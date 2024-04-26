package garry.community.controller;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import garry.community.annotation.LoginRequired;
import garry.community.consts.CommunityConst;
import garry.community.enums.CommentStatusEnum;
import garry.community.enums.DiscussPostStatusEnum;
import garry.community.enums.DiscussPostTypeEnum;
import garry.community.enums.EntityTypeEnum;
import garry.community.form.AddDiscussPostForm;
import garry.community.pojo.Comment;
import garry.community.pojo.DiscussPost;
import garry.community.pojo.User;
import garry.community.service.*;
import garry.community.utils.HostHolder;
import garry.community.utils.RedisKeyUtil;
import garry.community.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Garry
 * ---------2024/3/20 20:35
 **/
@Slf4j
@Controller
@RequestMapping(value = "/post")
public class DiscussPostController {
    @Resource
    private DiscussPostService discussPostService;

    @Resource
    private UserService userService;

    @Resource
    private CommentService commentService;

    @Resource
    private LikeService likeService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private ElasticsearchService elasticsearchService;

    @Resource
    private HostHolder hostHolder;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * 显示帖子详情页面
     *
     * @param model
     * @param postId
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String getDetailPage(Model model, Integer postId,
                                @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        //查询post
        DiscussPost post = discussPostService.findPostById(postId);
        if (post == null || post.getStatus().equals(DiscussPostStatusEnum.BLACKLIST.getCode())) {
            throw new RuntimeException("【帖子不存在】postId = " + gson.toJson(postId));
        }

        //查询user
        User user = userService.findByUserId(post.getUserId());
        if (user == null) {
            throw new RuntimeException("【用户不存在】userId = " + gson.toJson(post.getUserId()));
        }

        //查询该帖子的所有评论
        PageInfo<Comment> pageInfo = commentService.findComments(EntityTypeEnum.DISCUSS_POST, postId, pageNum, pageSize);
        List<Comment> commentList = pageInfo.getList();

        //获取以存有评论用户和评论实体的map为元素的List
        List<HashMap<String, Object>> comments = commentList.stream()
                .map(comment -> {
                    HashMap<String, Object> map = new HashMap<>();
                    //该帖子评论的user
                    User commentUser = userService.findByUserId(comment.getUserId());
                    //该评论下的所有评论
                    List<Comment> subCommentList = commentService
                            .findComments(EntityTypeEnum.COMMENT, comment.getId(), 1, 99999)
                            .getList();

                    //将该评论下的所有评论，以List<HashMap<String, Object>>形式保存
                    List<HashMap<String, Object>> subComments = subCommentList.stream()
                            .map(subComment -> {
                                HashMap<String, Object> subCommentMap = new HashMap<>();
                                User subCommentUser = userService.findByUserId(subComment.getUserId());

                                //查看subComment的target，如果target!=0，则是回复给帖子的评论的评论的，否则是回复给帖子的评论的
                                if (subComment.getTargetId() != 0) {
                                    //帖子的评论的评论回复的对象(根据targetId获取)
                                    User targetUser = userService.findByUserId(subComment.getTargetId());
                                    //将targetUser放入subCommentMap中
                                    subCommentMap.put("targetUser", targetUser);
                                } else {
                                    //此处必须输入targetUser=null，否则前端thymeleaf会因为找不到targetUser而报错！
                                    //即可以等于null，但是不能没有！
                                    subCommentMap.put("targetUser", null);
                                }

                                //查询帖子的评论的点赞数
                                long likeCount = likeService.findEntityLikeCount(EntityTypeEnum.COMMENT.getCode(), comment.getId());
                                //查询当前用户是否对该帖子的评论点赞
                                int likeStatus = likeService.findEntityLikeStatus(hostHolder.getUser() == null ? 0 : hostHolder.getUser().getId(), EntityTypeEnum.COMMENT.getCode(), comment.getId());

                                subCommentMap.put("user", subCommentUser);
                                subCommentMap.put("comment", subComment);
                                subCommentMap.put("likeCount", likeCount);
                                subCommentMap.put("likeStatus", likeStatus);
                                return subCommentMap;
                            }).collect(Collectors.toList());

                    //查询帖子的评论的点赞数
                    long likeCount = likeService.findEntityLikeCount(EntityTypeEnum.COMMENT.getCode(), comment.getId());
                    //查询当前用户是否对该帖子的评论点赞
                    int likeStatus = likeService.findEntityLikeStatus(hostHolder.getUser() == null ? 0 : hostHolder.getUser().getId(), EntityTypeEnum.COMMENT.getCode(), comment.getId());

                    //存入map
                    map.put("user", commentUser);
                    map.put("comment", comment);
                    map.put("subComments", subComments);
                    map.put("likeCount", likeCount);
                    map.put("likeStatus", likeStatus);
                    return map;
                }).collect(Collectors.toList());

        //查询帖子的点赞总数
        long likeCount = likeService.findEntityLikeCount(EntityTypeEnum.DISCUSS_POST.getCode(), post.getId());
        //查询当前用户是否点赞
        int likeStatus = likeService.findEntityLikeStatus(hostHolder.getUser() == null ? 0 : hostHolder.getUser().getId(), EntityTypeEnum.DISCUSS_POST.getCode(), post.getId());

        //传入帖子和帖子user
        model.addAttribute("post", post);
        model.addAttribute("postUser", user);
        //传入帖子的点赞总数，当前用户是否点赞，用于前端展示
        model.addAttribute("likeCount", likeCount);
        model.addAttribute("likeStatus", likeStatus);
        //传入帖子下所有评论的List
        model.addAttribute("comments", comments);
        //写入pageInfo，用于控制分页
        model.addAttribute("pageInfo", pageInfo);
        //写入分页的页面，便于分页模板的复用
        model.addAttribute("pagePath", "/post/detail");
        //传入分页导航栏每一边的长度
        model.addAttribute("navigatePageWidth", CommunityConst.NAVIGATE_PAGE_WIDTH);
        return "/site/discuss-detail";
    }

    /**
     * 接收用户点击发布帖子后，浏览器AJAX发来的异步请求
     *
     * @param addDiscussPostForm
     * @return
     */
    @LoginRequired
    @RequestMapping(value = "/add", method = RequestMethod.POST)//浏览器需要提交很多数据，因此用POST
    @ResponseBody//返回json字符串，而不是网页，实现异步
    public String addPost(AddDiscussPostForm addDiscussPostForm) {
        User user = hostHolder.getUser();
        DiscussPost discussPost = makeDiscussPost(addDiscussPostForm, user);

        int rows = discussPostService.addDiscussPost(discussPost);
        if (rows <= 0) {
            throw new RuntimeException("【帖子添加失败】 post = " + gson.toJson(discussPost));
        }

        //同步，将新增的帖子加入ES的对应节点中(视频中将发布的事件添加到消息队列，由另一Producer处理，实现异步添加到ES)
        elasticsearchService.addPost(discussPost);

        //将新增的帖子加到redis缓存中，以便统一计算分数
        String redisKey = RedisKeyUtil.getPostKey();
        redisTemplate.opsForSet().add(redisKey, discussPost.getId());

        return gson.toJson(ResponseVo.success());
    }

    /**
     * 置顶
     *
     * @param postId
     * @return
     */
    @LoginRequired
    @RequestMapping(value = "/top", method = RequestMethod.POST)
    @ResponseBody
    public String setTop(Integer postId) {
        DiscussPost post = discussPostService.findPostById(postId);
        post.setType(DiscussPostTypeEnum.TOP.getCode());
        discussPostService.update(post);

        //将最新的数据同步到ES中(视频中借助了消息队列，但是我这里图方便就直接同步请求处理了)
        elasticsearchService.addPost(post);

        return gson.toJson(ResponseVo.success());
    }

    /**
     * 加精
     *
     * @param postId
     * @return
     */
    @LoginRequired
    @RequestMapping(value = "/wonderful", method = RequestMethod.POST)
    @ResponseBody
    public String setWonderful(Integer postId) {
        DiscussPost post = discussPostService.findPostById(postId);
        post.setStatus(DiscussPostStatusEnum.WONDERFUL.getCode());
        discussPostService.update(post);

        //将最新的数据同步到ES中(视频中借助了消息队列，但是我这里图方便就直接同步请求处理了)
        elasticsearchService.addPost(post);

        //将加精的帖子加到redis缓存中，以便统一计算分数
        String redisKey = RedisKeyUtil.getPostKey();
        redisTemplate.opsForSet().add(redisKey, post.getId());

        return gson.toJson(ResponseVo.success());
    }

    /**
     * 拉黑
     *
     * @param postId
     * @return
     */
    @LoginRequired
    @RequestMapping(value = "/blacklist", method = RequestMethod.POST)
    @ResponseBody
    public String setBlacklist(Integer postId) {
        DiscussPost post = discussPostService.findPostById(postId);
        post.setStatus(DiscussPostStatusEnum.BLACKLIST.getCode());
        discussPostService.update(post);

        //帖子被拉黑后，它下面的所有评论(comment)都应该被改为无效
        List<Comment> commentList = commentService.findCommentByEntityId(postId);
        List<Comment> bannedCommentList = new ArrayList<>();
        if (commentList != null) {
            for (Comment comment : commentList) {
                bannedCommentList.add(comment);
                List<Comment> commentsOfComment = commentService.findCommentByEntityId(comment.getId());
                if (commentsOfComment != null) {
                    for (Comment c : commentsOfComment) {
                        bannedCommentList.add(c);
                    }
                }
            }
        }
        for (Comment comment : bannedCommentList) {
            comment.setStatus(CommentStatusEnum.BANNED.getCode());
            commentService.update(comment);
        }

        //将最新的数据同步到ES中(视频中借助了消息队列，但是我这里图方便就直接同步请求处理了)
        elasticsearchService.deletePost(post.getId());

        return gson.toJson(ResponseVo.success());
    }

    /**
     * 根据addDiscussPostForm和user创建DiscussPost
     *
     * @param addDiscussPostForm
     * @param user
     * @return
     */
    private DiscussPost makeDiscussPost(AddDiscussPostForm addDiscussPostForm, User user) {
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(addDiscussPostForm.getTitle());
        discussPost.setContent(addDiscussPostForm.getContent());
        discussPost.setType(DiscussPostTypeEnum.NORMAL.getCode());
        discussPost.setStatus(DiscussPostStatusEnum.NORMAL.getCode());
        discussPost.setCreateTime(new Date());
        discussPost.setCommentCount(0);
        discussPost.setScore(0.0);
        return discussPost;
    }
}
