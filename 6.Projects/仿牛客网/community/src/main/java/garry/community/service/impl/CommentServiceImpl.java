package garry.community.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import garry.community.dao.CommentMapper;
import garry.community.dao.DiscussPostMapper;
import garry.community.enums.EntityTypeEnum;
import garry.community.pojo.Comment;
import garry.community.pojo.DiscussPost;
import garry.community.service.CommentService;
import garry.community.utils.SensitiveFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Garry
 * ---------2024/3/21 13:44
 **/
@Slf4j
@Service
public class CommentServiceImpl implements CommentService {
    @Resource
    private CommentMapper commentMapper;

    @Resource
    private DiscussPostMapper discussPostMapper;

    @Resource
    private SensitiveFilter sensitiveFilter;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public Comment findCommentById(Integer commentId) {
        return commentMapper.selectByPrimaryKey(commentId);
    }

    @Override
    public int update(Comment comment) {
        return commentMapper.updateByPrimaryKeySelective(comment);
    }

    @Override
    public PageInfo<Comment> findComments(EntityTypeEnum entityTypeEnum, Integer entityId, Integer pageNum, Integer pageSize) {
        //启动分页
        PageHelper.startPage(pageNum, pageSize);

        //查询该实体的所有非禁用评论
        List<Comment> commentList = commentMapper.selectComments(entityTypeEnum.getCode(), entityId);

        //将查询结果加入pageInfo
        PageInfo<Comment> pageInfo = new PageInfo<>(commentList);
        pageInfo.setList(commentList);
        return pageInfo;
    }

    @Override
    public int findCommentRows(EntityTypeEnum entityTypeEnum, Integer entityId) {
        return commentMapper.selectCommentRows(entityTypeEnum.getCode(), entityId);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int addComment(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("【comment为空】");
        }

        //过滤评论
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));

        //更新帖子数量
        if (comment.getEntityType().equals(EntityTypeEnum.DISCUSS_POST.getCode())) {
            DiscussPost discussPost = discussPostMapper.selectByPrimaryKey(comment.getEntityId());
            discussPost.setCommentCount(commentMapper.selectCommentRows(EntityTypeEnum.DISCUSS_POST.getCode(), comment.getEntityId()) + 1);
            int rows = discussPostMapper.updateByPrimaryKeySelective(discussPost);
            if (rows <= 0) {
                throw new RuntimeException("【discussPost更新失败】discussPost = " + gson.toJson(discussPost));
            }
        }

        //插入评论
        int rows = commentMapper.insertSelective(comment);
        if (rows <= 0) {
            throw new RuntimeException("【comment插入失败】comment = " + gson.toJson(comment));
        }

        return rows;
    }

    @Override
    public int findCommentCountByUserId(Integer userId) {
        return commentMapper.selectCommentCountByUserId(userId);
    }

    @Override
    public PageInfo<Comment> findCommentsByUserId(Integer userId, Integer pageNum, Integer pageSize) {
        //启动分页
        PageHelper.startPage(pageNum, pageSize);

        //查询该实体的所有非禁用评论
        List<Comment> commentList = commentMapper.selectCommentByUserId(userId);

        //将查询结果加入pageInfo
        PageInfo<Comment> pageInfo = new PageInfo<>(commentList);
        pageInfo.setList(commentList);
        return pageInfo;
    }

    @Override
    public List<Comment> findCommentByEntityId(Integer entityId) {
        return commentMapper.selectCommentByEntityId(entityId);
    }
}
