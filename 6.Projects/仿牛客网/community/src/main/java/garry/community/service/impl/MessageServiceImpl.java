package garry.community.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import garry.community.consts.CommunityConst;
import garry.community.dao.MessageMapper;
import garry.community.pojo.Message;
import garry.community.service.MessageService;
import garry.community.utils.SensitiveFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Garry
 * ---------2024/3/22 17:06
 **/
@Slf4j
@Service
public class MessageServiceImpl implements MessageService {
    @Resource
    private MessageMapper messageMapper;

    @Resource
    private SensitiveFilter sensitiveFilter;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public Message findById(Integer messageId) {
        return messageMapper.selectByPrimaryKey(messageId);
    }

    @Override
    public int update(Message message) {
        return messageMapper.updateByPrimaryKeySelective(message);
    }

    @Override
    public PageInfo<Message> findConversations(Integer userId, Integer pageNum, Integer pageSize) {
        //启动分页
        PageHelper.startPage(pageNum, pageSize);

        //查询
        List<Message> messageList = messageMapper.selectConversations(userId);

        //将查询结果加入pageInfo
        PageInfo<Message> pageInfo = new PageInfo<>(messageList);
        pageInfo.setList(messageList);
        return pageInfo;
    }

    @Override
    public PageInfo<Message> findLetters(String conversationId, Integer pageNum, Integer pageSize) {
        //启动分页
        PageHelper.startPage(pageNum, pageSize);

        //查询
        List<Message> messageList = messageMapper.selectLetters(conversationId);

        //将查询结果加入pageInfo
        PageInfo<Message> pageInfo = new PageInfo<>(messageList);
        pageInfo.setList(messageList);
        return pageInfo;
    }

    @Override
    public int findUnreadLettersCount(Integer userId, String conversationId) {
        return messageMapper.selectUnreadLettersCount(userId, conversationId);
    }

    @Override
    public int findLettersCount(Integer userId, String conversationId) {
        return messageMapper.selectLettersCount(userId, conversationId);
    }

    @Override
    public int addMessage(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("【message为空】");
        }

        //过滤私信
        String conversationId = message.getConversationId();
        if (!conversationId.equals(CommunityConst.TOPIC_COMMENT)
                && !conversationId.equals(CommunityConst.TOPIC_LIKE)
                && !conversationId.equals(CommunityConst.TOPIC_FOLLOW)) {
            message.setContent(HtmlUtils.htmlEscape(message.getContent()));
            message.setContent(sensitiveFilter.filter(message.getContent()));
        }

        //插入私信
        int rows = messageMapper.insertSelective(message);
        if (rows <= 0) {
            throw new RuntimeException("【message插入失败】message = " + gson.toJson(message));
        }

        return rows;
    }

    @Override
    public int findUnreadNoticeCount(Integer userId) {
        return messageMapper.selectUnreadNoticeCount(userId);
    }

    @Override
    public int findUnreadSpecificNoticeCount(Integer userId, String noticeTopic) {
        return messageMapper.selectUnreadSpecificNoticeCount(userId, noticeTopic);
    }

    @Override
    public Message findRecentMessageOfSpecificNotice(Integer userId, String noticeTopic) {
        return messageMapper.selectRecentMessageOfSpecificNotice(userId, noticeTopic);
    }

    @Override
    public int findSpecificNoticeCount(Integer userId, String noticeTopic) {
        return messageMapper.selectSpecificNoticeCount(userId, noticeTopic);
    }

    @Override
    public PageInfo<Message> findSpecificNotice(Integer userId, String noticeTopic, Integer pageNum, Integer pageSize) {
        //启动分页
        PageHelper.startPage(pageNum, pageSize);

        //查询该用户的某一特定系统通知下的所有通知
        List<Message> messageList = messageMapper.selectSpecificNotice(userId, noticeTopic);

        //将查询结果加入pageInfo
        PageInfo<Message> pageInfo = new PageInfo<>(messageList);
        pageInfo.setList(messageList);
        return pageInfo;
    }

    @Override
    public Map<String, Object> getMapFromContent(String content) {
        return gson.fromJson(content, HashMap.class);
    }
}
