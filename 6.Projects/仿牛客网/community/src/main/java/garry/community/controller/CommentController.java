package garry.community.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import garry.community.annotation.LoginRequired;
import garry.community.consts.CommunityConst;
import garry.community.enums.CommentStatusEnum;
import garry.community.enums.EntityTypeEnum;
import garry.community.event.EventProducer;
import garry.community.form.AddCommentForm;
import garry.community.pojo.Comment;
import garry.community.pojo.DiscussPost;
import garry.community.pojo.Event;
import garry.community.service.CommentService;
import garry.community.service.DiscussPostService;
import garry.community.service.ElasticsearchService;
import garry.community.utils.HostHolder;
import garry.community.utils.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author Garry
 * ---------2024/3/21 19:50
 **/
@Slf4j
@Controller
@RequestMapping(value = "/comment")
public class CommentController {
    @Resource
    private CommentService commentService;

    @Resource
    private DiscussPostService discussPostService;

    @Resource
    private HostHolder hostHolder;

    @Resource
    private EventProducer eventProducer;

    @Resource
    private ElasticsearchService elasticsearchService;

    @Resource
    private RedisTemplate redisTemplate;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * 接收浏览器AJAX的异步请求，添加评论，
     * 如果评论的实体是帖子，还会增加帖子的评论数
     *
     * @param addCommentForm
     * @return
     */
    @LoginRequired
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addComment(AddCommentForm addCommentForm) {
        if (addCommentForm == null) {
            throw new IllegalArgumentException("【addCommentForm为null】");
        }

        //创建comment
        Comment comment = new Comment();
        BeanUtils.copyProperties(addCommentForm, comment);
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(CommentStatusEnum.NORMAL.getCode());
        comment.setCreateTime(new Date());

        //添加comment以及post的回帖数
        int rows = commentService.addComment(comment);
        if (rows <= 0) {
            throw new RuntimeException("【comment更新失败】comment = " + gson.toJson(comment));
        }

        //将评论数更新的帖子传入ES(也可以通过消息队列异步传过去，这样性能会高一点，因为只需要放到消息队列里就可以继续执行了)
        DiscussPost post = null;
        if (comment.getEntityType().equals(EntityTypeEnum.DISCUSS_POST.getCode())) {
            post = discussPostService.findPostById(comment.getEntityId());
        } else if (comment.getEntityType().equals(EntityTypeEnum.COMMENT.getCode())) {
            Comment preComment = commentService.findCommentById(comment.getEntityId());
            if (preComment.getEntityType().equals(EntityTypeEnum.DISCUSS_POST.getCode())) {
                post = discussPostService.findPostById(preComment.getEntityId());
            } else {
                Comment prePreComment = commentService.findCommentById(preComment.getEntityId());
                post = discussPostService.findPostById(prePreComment.getEntityId());
            }
        }
        if (post != null) {//ES会自动按照postId去update对应的post
            elasticsearchService.addPost(post);
        }

        //添加系统通知
        Event event = addCommentFormToEvent(addCommentForm);
        eventProducer.fireEvent(event);

        //新增评论后的帖子加到重新算分的缓存中
        if (comment.getEntityType().equals(EntityTypeEnum.DISCUSS_POST.getCode())) {
            String redisKey = RedisKeyUtil.getPostKey();
            redisTemplate.opsForSet().add(redisKey, comment.getEntityId());
        }

        return "redirect:/post/detail" + "?postId=" + addCommentForm.getPostId();
    }

    /**
     * 获取Event
     *
     * @param addCommentForm
     * @return
     */
    private Event addCommentFormToEvent(AddCommentForm addCommentForm) {
        Event event = new Event();
        event.setTopic(CommunityConst.TOPIC_COMMENT);
        event.setUserId(hostHolder.getUser().getId());
        event.setEntityType(addCommentForm.getEntityType());
        event.setEntityId(addCommentForm.getEntityId());

        //分类讨论评论的实体类型，设置entityUserId
        Integer entityType = addCommentForm.getEntityType();
        //评论对象是帖子
        if (entityType.equals(EntityTypeEnum.DISCUSS_POST.getCode())) {
            DiscussPost post = discussPostService.findPostById(addCommentForm.getEntityId());
            event.setEntityUserId(post.getUserId());
            //评价的对象是评论
        } else if (entityType.equals(EntityTypeEnum.COMMENT.getCode())) {
            //评论的是帖子的评论
            if (addCommentForm.getTargetId().equals(0)) {
                Comment comment = commentService.findCommentById(addCommentForm.getEntityId());
                event.setEntityUserId(comment.getUserId());
            } else {//评论的是评论的评论
                event.setEntityUserId(addCommentForm.getTargetId());
            }
        } else {
            throw new RuntimeException("【评论的实体不存在，无法转换为Event】");
        }

        event.setData("postId", addCommentForm.getPostId());

        return event;
    }
}
