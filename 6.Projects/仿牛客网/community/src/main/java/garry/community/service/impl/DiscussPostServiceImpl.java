package garry.community.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import garry.community.dao.DiscussPostMapper;
import garry.community.pojo.DiscussPost;
import garry.community.service.DiscussPostService;
import garry.community.utils.SensitiveFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Garry
 * ---------2024/3/15 20:20
 **/
@Slf4j
@Service
public class DiscussPostServiceImpl implements DiscussPostService {
    @Resource
    private DiscussPostMapper discussPostMapper;

    @Resource
    private SensitiveFilter sensitiveFilter;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

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

    @Override
    public PageInfo<DiscussPost> findHotDiscussPosts(Integer userId, Integer pageNum, Integer pageSize, Boolean onTop) {
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
