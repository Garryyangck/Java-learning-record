package garry.community.utils;

/**
 * @author Garry
 * ---------2024/3/20 13:03
 **/

import com.google.gson.GsonBuilder;
import garry.community.consts.CommunityConst;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 通用敏感词过滤器
 */
@Slf4j
@Component
public class SensitiveFilter {

    private final TrieNode root = new TrieNode();

    /**
     * 过滤敏感词，将敏感词替换为SENSITIVE_WORDS_REPLACEMENT
     *
     * @param message 需要过滤敏感词的信息
     * @return 完成敏感词过滤的信息(如果信息为空则返回null)
     */
    public String filter(String message) {
        if (StringUtils.isBlank(message)) {
            return null;
        }
        StringBuffer buffer = new StringBuffer();

        //左指针不会回退，右指针会在左指针的基础上右移并回退
        for (int left = 0; left < message.length(); left++) {
            //left为特殊字符，肯定不是敏感词
            if (isSymbol(message.charAt(left))) {
                buffer.append(message.charAt(left));
                continue;
            }

            //右节点初始化为left
            int right = left;
            //替换的结束位置，用于left=end-1
            //避免重复发现敏感字(如中国政治中，中国政治和政治会算两次敏感字)
            int end = left;
            //以left开头的字符串是否存在敏感词
            boolean hasSensitiveWord = false;
            //用于前缀树结点的遍历
            TrieNode nowNode = this.root;
            //右节点从左节点开始向右遍历
            while (right < message.length()) {
                //右结点当前的字符
                char c = message.charAt(right);

                //c为特殊符号，前缀树不做出改变
                if (isSymbol(c)) {
                    right++;
                    continue;
                }

                //没有特殊符号了，都是有效字符
                TrieNode subNode = nowNode.getSubNode(c);
                //没有搜到敏感字
                if (subNode == null) {
                    //必须以left开头的字符没有敏感词，
                    //才将message.charAt(left)加入buffer
                    if (!hasSensitiveWord)
                        buffer.append(message.charAt(left));
                    //退出循环
                    break;
                } else {//以left开头的字符串可能还存在敏感词
                    //left~right是敏感词
                    if (subNode.isKeywordEnd()) {
                        hasSensitiveWord = true;
                        //更新当前以left开头的字符串中的最长敏感词
                        end = right + 1;
                    }
                    right++;
                    nowNode = subNode;
                }
            }//while
            if (hasSensitiveWord) {
                //将以left开头的字符串中的最长敏感词替换为SENSITIVE_WORDS_REPLACEMENT
                buffer.append(CommunityConst.SENSITIVE_WORDS_REPLACEMENT);
                //避免重复计算子敏感词，如(中国政治存在子敏感词政治，但只算一个敏感词)
                left = end - 1;
            }
        }

        return new String(buffer);
    }

    /**
     * 从sensitive-words.txt中读取敏感词，并将其添加到本Bean的前缀树中
     */
    @PostConstruct//在实例化之后自动调用该方法
    private void init() {
        try (
                InputStream inputStream = this.getClass()//获取Class类
                        .getClassLoader()//获取类加载器
                        //在classes目录下获取"sensitive-words.txt"的字节输入流
                        .getResourceAsStream("sensitive-words.txt");
                //使用字符包装流
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream, StandardCharsets.UTF_8))
        ) {
            //读取敏感词，一个词占一行
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                //添加到前缀树
                this.addKeyword(keyword);
            }
            log.info("\n前缀树所有敏感词 = {}", new GsonBuilder().setPrettyPrinting().create()
                    .toJson(this.showKeywords()));
        } catch (IOException e) {
            log.error("【加载敏感词文件失败】" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 将敏感词添加到前缀树
     *
     * @param keyword
     */
    private void addKeyword(String keyword) {
        //当前结点的位置
        TrieNode nowNode = this.root;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode subNode = nowNode.getSubNode(c);
            //当前结点的子结点Map没有此字符，则添加该字符到Map中
            if (subNode == null) {
                //新节点
                subNode = new TrieNode();
                //如果是最后一个字符，则isKeywordEnd设置为true
                if (i == keyword.length() - 1) {
                    subNode.setKeywordEnd(true);
                }
                nowNode.addSubNode(c, subNode);
            }
            nowNode = subNode;
        }
    }

    /**
     * 返回前缀树中所有敏感词的集合
     *
     * @return
     */
    public List<String> showKeywords() {
        List<String> ans = new ArrayList<>();
        StringBuffer keyword = new StringBuffer();
        showKeywordsHelper(this.root, keyword, ans);
        return ans;
    }

    /**
     * 使用回溯算法找出前缀树所有敏感词
     *
     * @param root
     * @param keyword
     * @param ans
     */
    private void showKeywordsHelper(TrieNode root, StringBuffer keyword, List<String> ans) {
        if (root.isKeywordEnd()) {
            ans.add(new String(keyword));
        }

        Set<Map.Entry<Character, TrieNode>> entrySet = root.subNodes.entrySet();
        for (Map.Entry<Character, TrieNode> entry : entrySet) {
            keyword.append(entry.getKey());
            showKeywordsHelper(entry.getValue(), keyword, ans);
            keyword.deleteCharAt(keyword.length() - 1);
        }
    }

    /**
     * 判断是否是特殊符号，防止用户输入存在"我.是.一.条.很.长.的.敏.感.词"的情况
     *
     * @param c
     * @return
     */
    public boolean isSymbol(Character c) {
        //isAsciiAlphanumeric(是正常数字或字母)，0x2E80 ~ 0x9FFF是东亚文字范围
        //即特殊符号：不是正常的数字或字母 且 不在东亚文字范围内
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    /**
     * 前缀树结点
     */
    private static class TrieNode {
        //敏感词结束的标识
        private boolean isKeywordEnd = false;

        //子节点Map
        private final Map<Character, TrieNode> subNodes = new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        //添加子结点
        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        //获取子结点
        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }
    }
}
