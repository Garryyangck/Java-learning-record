package garry.community.service.impl;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import garry.community.dao.DiscussPostMapper;
import garry.community.pojo.DiscussPost;
import garry.community.service.DiscussPostService;
import garry.community.utils.SensitiveFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Garry
 * ---------2024/3/31 12:15
 **/

/**
 * 使用caffeine进行本地缓存
 */
@Slf4j
@Primary
@Service
public class DiscussPostServiceCaffeineImpl implements DiscussPostService {
    @Resource
    private DiscussPostMapper discussPostMapper;

    @Resource
    private SensitiveFilter sensitiveFilter;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Value("${caffeine.posts.max-size}")
    private Integer maxSize;

    @Value("${caffeine.posts.expire-seconds}")
    private Long expireSeconds;

    //帖子缓存列表(每一页一个String，最多缓存maxSize页数据)
    private LoadingCache<String, PageInfo<DiscussPost>> postListCache;

    /**
     * 初始化帖子列表缓存
     */
    @PostConstruct
    private void init() {
        postListCache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                .build(new CacheLoader<String, PageInfo<DiscussPost>>() {
                    /**
                     * 如果Caffeine发现你的key在本地缓存(一级缓存)中没有对应的value，
                     * 那么它就会调用此方法，通过key获取PageInfo<DiscussPost>，并放入本地缓存
                     * @param key
                     * @return
                     * @throws Exception
                     */
                    @Override
                    public PageInfo<DiscussPost> load(String key) throws Exception {
                        if (StringUtils.isBlank(key)) {
                            throw new RuntimeException("【key为空，无法从本地缓存中进行查找】");
                        }

                        String[] params = key.split(":");
                        if (params == null || params.length != 2) {
                            throw new RuntimeException("【key的格式不正确，无法从本地缓存中进行查找】");
                        }

                        /*
                            在这里可以加上二级缓存，优先去访问redis，最后去访问数据库
                         */

                        int pageNum = Integer.parseInt(params[0]);
                        int pageSize = Integer.parseInt(params[1]);

                        log.info("【从数据库中查找热门帖子，pageNum = {}，pageSize = {}】", pageNum, pageSize);

                        //启动分页
                        PageHelper.startPage(pageNum, pageSize);

                        //查询首页热门帖子(需要置顶)
                        List<DiscussPost> discussPostList = discussPostMapper.selectHotDiscussPosts(0, true);

                        //将查询结果加入pageInfo
                        PageInfo<DiscussPost> pageInfo = new PageInfo<>(discussPostList);
                        pageInfo.setList(discussPostList);

                        return pageInfo;
                    }
                });
    }

    @Override
    public DiscussPost findPostById(Integer id) {
        return discussPostMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(DiscussPost post) {
        return discussPostMapper.updateByPrimaryKeySelective(post);
    }

    @Override
    public PageInfo<DiscussPost> findDiscussPosts(Integer userId, Integer pageNum, Integer pageSize, Boolean onTop) {
        //启动分页
        PageHelper.startPage(pageNum, pageSize);

        //查询该用户的所有非拉黑帖子
        List<DiscussPost> discussPostList = discussPostMapper.selectDiscussPosts(userId, onTop);

        //将查询结果加入pageInfo
        PageInfo<DiscussPost> pageInfo = new PageInfo<>(discussPostList);
        pageInfo.setList(discussPostList);
        return pageInfo;
    }

    /**
     * 查找热门帖子，使用Caffeine进行本地缓存
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param onTop
     * @return
     */
    @Override
    public PageInfo<DiscussPost> findHotDiscussPosts(Integer userId, Integer pageNum, Integer pageSize, Boolean onTop) {
        //userId == 0时，优先从缓存中获取
        if (userId == 0) {
            log.info("【从本地缓存中获取热门帖子，key = {}】", pageNum + ":" + pageSize);
            return postListCache.get(/*key:用分页数据约束*/pageNum + ":" + pageSize);
        }

        log.info("【从数据库中查找热门帖子，userId = {}，pageNum = {}，pageSize = {}】", userId, pageNum, pageSize);

        //启动分页
        PageHelper.startPage(pageNum, pageSize);

        //查询该用户的所有非拉黑帖子
        List<DiscussPost> discussPostList = discussPostMapper.selectHotDiscussPosts(userId, onTop);

        //将查询结果加入pageInfo
        PageInfo<DiscussPost> pageInfo = new PageInfo<>(discussPostList);
        pageInfo.setList(discussPostList);
        return pageInfo;
    }

    @Override
    public int findDiscussPostRows(Integer userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    @Override
    public int addDiscussPost(DiscussPost post) {
        if (post == null) {
            throw new IllegalArgumentException("【post为空】");
        }

        //转义HTML标签，防止恶意注入
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));

        //过滤敏感词
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));

        int rows = discussPostMapper.insertSelective(post);
        if (rows <= 0) {
            throw new RuntimeException("【帖子添加失败】post = " + gson.toJson(post));
        }

        return rows;
    }
}
