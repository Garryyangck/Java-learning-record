package garry.community.controller;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import garry.community.annotation.LoginRequired;
import garry.community.consts.CommunityConst;
import garry.community.enums.EntityTypeEnum;
import garry.community.enums.MistakeEnum;
import garry.community.enums.MsgEnum;
import garry.community.enums.ResponseEnum;
import garry.community.form.UpdatePasswordForm;
import garry.community.pojo.Comment;
import garry.community.pojo.DiscussPost;
import garry.community.pojo.Page;
import garry.community.pojo.User;
import garry.community.service.*;
import garry.community.utils.CommunityUtil;
import garry.community.utils.HostHolder;
import garry.community.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Garry
 * ---------2024/3/19 15:01
 **/
@Slf4j
@Controller
@RequestMapping(value = "/user")
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private LikeService likeService;

    @Resource
    private FollowService followService;

    @Resource
    private DiscussPostService discussPostService;

    @Resource
    private CommentService commentService;

    @Resource
    private HostHolder hostHolder;

    @Value("${community.path.domain}")
    private String domain;//域名

    @Value("${server.servlet.context-path}")
    private String contextPath;//上下文地址

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${qiniu.key.access}")
    private String accessKey;

    @Value("${qiniu.key.secret}")
    private String secretKey;

    @Value("${qiniu.bucket.header.name}")
    private String headerBucketName;

    @Value("${qiniu.bucket.header.url}")
    private String headerBucketUrl;

    private final Gson gson = new Gson();

    /**
     * 显示账号设置页面，并生成云服务器的上传凭证
     *
     * @return
     */
    @LoginRequired
    @RequestMapping(value = "/setting", method = RequestMethod.GET)
    public String getSettingPage(Model model) {
        /*
            表单直接提交给七牛云，他会检查你是否有登录凭证。
            所以在进入setting页面之前(不管用户会不会更新头像)，
            我们先生成随机文件名，并获取上传凭证，将其传给前端，以便表单提交。
         */
        //上传文件名称
        String fileName = CommunityUtil.generateUUID();
        //设置响应信息
        StringMap policy = new StringMap();
        policy.put("returnBody", gson.toJson(ResponseVo.success()));
        //生成上传凭证
        Auth auth = Auth.create(accessKey, secretKey);//通过AK和SK生成权限认证
        String uploadToken = auth.uploadToken(headerBucketName, fileName, 3600, policy);//上传凭证

        model.addAttribute("fileName", fileName);
        model.addAttribute("uploadToken", uploadToken);

        return "/site/setting";
    }

    /**
     * 收到云服务器上传成功的回复后，将对应user的headerUrl修改为headerBucketUrl + "/" + fileName
     *
     * @param fileName
     * @return
     */
    @LoginRequired
    @RequestMapping(value = "/update/header", method = RequestMethod.POST)
    @ResponseBody
    public String updateHeaderUrl(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return gson.toJson(ResponseVo.error(ResponseEnum.FILENAME_NULL));
        }

        String newHeaderUrl = headerBucketUrl + "/" + fileName;
        int rows = userService.updateHeader(hostHolder.getUser().getId(), newHeaderUrl);
        if (rows <= 0) {
            throw new RuntimeException("【用户头像链接更新失败】user = " + gson.toJson(hostHolder.getUser()));
        }

        return gson.toJson(ResponseVo.success());
    }

    /**
     * 显示用户个人主页
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String getProfilePage(Model model, Integer userId) {
        User user = userService.findByUserId(userId);
        if (user == null) {
            throw new RuntimeException("【user不存在】");
        }
        model.addAttribute("user", user);
        model.addAttribute("likeCount", likeService.findUserLikeCount(userId));
        model.addAttribute("followerCount", followService.findFollowerCount(EntityTypeEnum.USER.getCode(), userId));
        model.addAttribute("followeeCount", followService.findFolloweeCount(userId, EntityTypeEnum.USER.getCode()));
        int followStatus = followService.findFollowStatus(
                hostHolder.getUser() == null ? 0 : hostHolder.getUser().getId(), EntityTypeEnum.USER.getCode(), userId);
        model.addAttribute("followStatus", followStatus);
        return "/site/profile";
    }

    /**
     * 显示个人信息user的粉丝列表
     *
     * @param userId
     * @param model
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/follower", method = RequestMethod.GET)
    public String getFollowerPage(Integer userId, Model model,
                                  @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                  @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        User user = userService.findByUserId(userId);
        if (user == null) {
            throw new RuntimeException("【user不存在】");
        }

        Set<Integer> followerSet = followService.findFollowerSet(EntityTypeEnum.USER.getCode(), userId, pageNum, pageSize);
        List<Map<String, Object>> followers = followerSet.stream()
                .map(followerId -> {
                    Map<String, Object> map = new HashMap<>();
                    User follower = userService.findByUserId(followerId);
                    //注意：这里显示的是正在进行操作的用户对于followerId的关注状态
                    int followStatus = followService.findFollowStatus(
                            hostHolder.getUser() == null ? 0 : hostHolder.getUser().getId(), EntityTypeEnum.USER.getCode(), followerId);
                    //注意：这里显示的是粉丝什么时候关注该user的(而不是什么时候关注正在进行操作的用户的！)
                    long followDateTime = followService.findFollowDateTime(
                            followerId, EntityTypeEnum.USER.getCode(), userId);
                    map.put("user", follower);
                    map.put("followStatus", followStatus);
                    map.put("followDateTime", new Date(followDateTime));
                    return map;
                })
                .collect(Collectors.toList());

        Page page = new Page();
        page.setPageNum(pageNum);
        page.setPrePage(pageNum - 1);
        page.setNextPage(pageNum + 1);
        long followerCount = followService.findFollowerCount(EntityTypeEnum.USER.getCode(), userId);
        page.setPages(followerCount == 0 ? 0 : (int) (followerCount / pageSize + 1));

        //写入followers
        model.addAttribute("followers", followers);
        //向模板中写入page，用于控制分页
        model.addAttribute("pageInfo", page);
        //写入分页的页面，便于分页模板的复用
        model.addAttribute("pagePath", "/user/follower");
        //传入user
        model.addAttribute("user", user);
        //传入分页导航栏每一边的长度
        model.addAttribute("navigatePageWidth", CommunityConst.NAVIGATE_PAGE_WIDTH);

        return "/site/follower";
    }

    /**
     * 显示个人信息user所关注的人列表
     *
     * @param userId
     * @param model
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/followee", method = RequestMethod.GET)
    public String getFolloweePage(Integer userId, Model model,
                                  @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                  @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        User user = userService.findByUserId(userId);
        if (user == null) {
            throw new RuntimeException("【user不存在】");
        }

        Set<Integer> followeeSet = followService.findFolloweeSet(userId, EntityTypeEnum.USER.getCode(), pageNum, pageSize);
        List<Map<String, Object>> followees = followeeSet.stream()
                .map(followeeId -> {
                    Map<String, Object> map = new HashMap<>();
                    User followee = userService.findByUserId(followeeId);
                    //注意：这里显示的是正在进行操作的用户对于followeeId的关注状态
                    int followStatus = followService.findFollowStatus(
                            hostHolder.getUser() == null ? 0 : hostHolder.getUser().getId(), EntityTypeEnum.USER.getCode(), followeeId);
                    //注意：这里显示的是user什么时候关注这些followeeId的(而不是用户什么时候关注这些followeeId的！)
                    long followDateTime = followService.findFollowDateTime(
                            userId, EntityTypeEnum.USER.getCode(), followeeId);
                    map.put("user", followee);
                    map.put("followStatus", followStatus);
                    map.put("followDateTime", new Date(followDateTime));
                    return map;
                })
                .collect(Collectors.toList());

        Page page = new Page();
        page.setPageNum(pageNum);
        page.setPrePage(pageNum - 1);
        page.setNextPage(pageNum + 1);
        long followeeCount = followService.findFolloweeCount(userId, EntityTypeEnum.USER.getCode());
        page.setPages(followeeCount == 0 ? 0 : (int) (followeeCount / pageSize + 1));

        //写入followees
        model.addAttribute("followees", followees);
        //向模板中写入page，用于控制分页
        model.addAttribute("pageInfo", page);
        //写入分页的页面，便于分页模板的复用
        model.addAttribute("pagePath", "/user/followee");
        //传入user
        model.addAttribute("user", user);
        //传入分页导航栏每一边的长度
        model.addAttribute("navigatePageWidth", CommunityConst.NAVIGATE_PAGE_WIDTH);

        return "/site/followee";
    }

    /**
     * 显示登录用户的帖子的列表
     *
     * @param model
     * @param pageNum
     * @param pageSize
     * @return
     */
    @LoginRequired
    @RequestMapping(value = "/post", method = RequestMethod.GET)
    public String getMyPostPage(Model model,
                                @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        User user = hostHolder.getUser();

        PageInfo<DiscussPost> pageInfo = discussPostService.findDiscussPosts(user.getId(), pageNum, pageSize, false);
        List<DiscussPost> discussPostList = pageInfo.getList();
        List<Map<String, Object>> discussPosts = discussPostList.stream()
                .map(discussPost -> {
                    Map<String, Object> map = new HashMap<>();
                    //获取帖子的点赞数
                    long postLikeCount = likeService.findEntityLikeCount(EntityTypeEnum.DISCUSS_POST.getCode(), discussPost.getId());
                    map.put("post", discussPost);
                    map.put("likeCount", postLikeCount);
                    return map;
                }).collect(Collectors.toList());

        //向模板中写入discussPosts，用于遍历显示帖子
        model.addAttribute("discussPosts", discussPosts);
        //传入用户帖子总数
        model.addAttribute("postCount", discussPostService.findDiscussPostRows(user.getId()));
        //向模板中写入pageInfo，用于控制分页
        model.addAttribute("pageInfo", pageInfo);
        //写入分页的页面，便于分页模板的复用
        model.addAttribute("pagePath", "/user/post");
        //传入user，以便前端分页跳转时，可以跳到正确的user的页面
        model.addAttribute("user", user);
        //传入分页导航栏每一边的长度
        model.addAttribute("navigatePageWidth", CommunityConst.NAVIGATE_PAGE_WIDTH);
        return "/site/my-post";
    }

    /**
     * 显示登录用户的回复列表
     *
     * @param model
     * @param pageNum
     * @param pageSize
     * @return
     */
    @LoginRequired
    @RequestMapping(value = "/reply", method = RequestMethod.GET)
    public String getMyReplyPage(Model model,
                                 @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                 @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        User user = hostHolder.getUser();

        PageInfo<Comment> pageInfo = commentService.findCommentsByUserId(user.getId(), pageNum, pageSize);
        List<Comment> commentList = pageInfo.getList();
        List<Map<String, Object>> comments = commentList.stream()
                .map(comment -> {
                    Map<String, Object> map = new HashMap<>();

                    //分类讨论，获取postId
                    Integer postId = null;
                    Integer entityType = comment.getEntityType();
                    //评论的是帖子
                    if (entityType.equals(EntityTypeEnum.DISCUSS_POST.getCode())) {
                        postId = comment.getEntityId();
                        //评论的是其它评论
                    } else if (entityType.equals(EntityTypeEnum.COMMENT.getCode())) {
                        //获取你所评论的评论
                        Comment preComment = commentService.findCommentById(comment.getEntityId());
                        //获取preComment的评论实体，只有两种情况(帖子的评论，评论的评论)
                        entityType = preComment.getEntityType();
                        //preComment的评论实体为帖子
                        if (entityType.equals(EntityTypeEnum.DISCUSS_POST.getCode())) {
                            postId = preComment.getEntityId();
                        } else {
                            //preComment的评论实体依然是评论，那么prePreComment的实体一定是贴子
                            Comment prePreComment = commentService.findCommentById(preComment.getEntityId());
                            postId = prePreComment.getEntityId();
                        }
                    }
                    //WARN 可能出现空指针异常
                    DiscussPost post = discussPostService.findPostById(postId);
                    map.put("post", post);
                    map.put("comment", comment);
                    return map;
                }).collect(Collectors.toList());


        //向模板中写入discussPosts，用于遍历显示帖子
        model.addAttribute("comments", comments);
        //传入用户评论总数
        model.addAttribute("commentCount", commentService.findCommentCountByUserId(user.getId()));
        //向模板中写入pageInfo，用于控制分页
        model.addAttribute("pageInfo", pageInfo);
        //写入分页的页面，便于分页模板的复用
        model.addAttribute("pagePath", "/user/reply");
        //传入user，以便前端分页跳转时，可以跳到正确的user的页面
        model.addAttribute("user", user);
        //传入分页导航栏每一边的长度
        model.addAttribute("navigatePageWidth", CommunityConst.NAVIGATE_PAGE_WIDTH);
        return "/site/my-reply";
    }

    /**
     * 接收用户上传的图像文件，以随机文件名存到服务器本地，并修改用户的headerUrl
     *
     * @param headerImage
     * @param model
     * @return
     */
    @Deprecated//现在直接将表单提交到七牛云服务器上，不再存储在本地了，因此该方法过时
    @LoginRequired
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String updateHeader(MultipartFile headerImage, Model model) {
        /*
            1.headerImage判空
         */
        if (headerImage == null) {
            model.addAttribute(MsgEnum.FILE_MSG.getMsg(), MistakeEnum.FILE_IS_NULL.getDesc());
            return "/site/setting";
        }

        /*
            2.获取文件后缀，后缀不能为空
         */
        String filename = headerImage.getOriginalFilename();
        assert filename != null;
        //后缀
        String suffix = filename.substring(filename.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute(MsgEnum.FILE_MSG.getMsg(), MistakeEnum.WRONG_FILE_PATTERN.getDesc());
            return "/site/setting";
        }

        /*
            3.生成随机文件名，避免不同用户文件名重复
         */
        filename = CommunityUtil.generateUUID()
                .substring(0, CommunityConst.RANDOM_FILE_NAME_LENGTH)
                + suffix;
        //该文件是存在服务器上的，用户访问需要通过web访问服务器，然后服务器查找该文件用于返回
        File dest = new File(uploadPath + "/" + filename);

        /*
            4.将图片内容写入dest，完成文件的存储
         */
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            log.error("【文件上传失败 " + e.getMessage() + "】headerImage = {}", gson.toJson(headerImage));
            e.printStackTrace();
            throw new RuntimeException("【文件上传失败】", e);
        }

        /*
            5.更新用户的headerUrl
            http://localhost:8080/community/user/header/xxx.png
         */
        String headerUrl = domain + contextPath + "/user/header/" + filename;
        User user = hostHolder.getUser();
        int rows = userService.updateHeader(user.getId(), headerUrl);
        if (rows <= 0) {
            throw new RuntimeException("【用户更新失败】user = " + gson.toJson(user));
        }

        return "redirect:/index";
    }

    /**
     * 页面头部通过user.headUrl发出请求，根据路径上的filename找到文件(头像图片)，传输给浏览器头部
     *
     * @param filename
     * @param response
     */
    @Deprecated//现在直接将表单提交到七牛云服务器上，不再存储在本地了，因此该方法过时
    @RequestMapping(value = "/header/{filename}", method = RequestMethod.GET)
    public void getHeader(@PathVariable(value = "filename") String filename,
                          HttpServletResponse response) {
        //服务器上的文件路径
        filename = uploadPath + "/" + filename;
        //后缀名
        String suffix = filename.substring(filename.lastIndexOf("."));

        response.setContentType("image/" + suffix);
        try (//jdk7新语法：自动关闭(close)
             //获取传给浏览器的输出流
             ServletOutputStream os = response.getOutputStream();
             //指定文件的输入流
             FileInputStream fis = new FileInputStream(new File(filename));
        ) {
            byte[] buf = new byte[1024];
            int readLen = 0;
            while ((readLen = fis.read(buf)) != -1) {
                os.write(buf, 0, readLen);
            }
        } catch (IOException e) {
            log.error("【向浏览器传输头像图片失败】");
            e.printStackTrace();
            throw new RuntimeException("【向浏览器传输头像图片失败】", e);
        }
    }

    /**
     * 修改密码
     *
     * @param model
     * @param updatePasswordForm
     * @return
     */
    @LoginRequired
    @RequestMapping(value = "/update/password", method = RequestMethod.POST)
    public String updatePassword(Model model, UpdatePasswordForm updatePasswordForm) {
        if (updatePasswordForm == null) {
            throw new IllegalArgumentException("【updatePasswordForm不能为空】");
        }

        //新旧密码存在控制空值
        if (StringUtils.isBlank(updatePasswordForm.getOldPassword()) || StringUtils.isBlank(updatePasswordForm.getOldPassword())) {
            if (StringUtils.isBlank(updatePasswordForm.getOldPassword())) {
                model.addAttribute(MsgEnum.PASSWORD_MSG.getMsg(), MistakeEnum.PASSWORD_BLANK.getDesc());
            }
            if (StringUtils.isBlank(updatePasswordForm.getOldPassword())) {
                model.addAttribute(MsgEnum.NEW_PASSWORD_MSG.getMsg(), MistakeEnum.NEW_PASSWORD_BLANK.getDesc());
            }
            return "/site/setting";
        }

        //新密码长度不足
        if (updatePasswordForm.getNewPassword().length() < CommunityConst.MIN_PASSWORD_LENGTH) {
            model.addAttribute(MsgEnum.NEW_PASSWORD_MSG.getMsg(), MistakeEnum.NEW_PASSWORD_TOO_SHORT.getDesc());
            return "/site/setting";
        }

        //旧密码错误
        User user = hostHolder.getUser();
        String inputOldPassword = CommunityUtil.md5Encryption(updatePasswordForm.getOldPassword() + user.getSalt());
        if (inputOldPassword == null || !inputOldPassword.equals(user.getPassword())) {
            model.addAttribute(MsgEnum.PASSWORD_MSG.getMsg(), MistakeEnum.WRONG_PASSWORD.getDesc());
            return "/site/setting";
        }

        String newPassword = CommunityUtil.md5Encryption(updatePasswordForm.getNewPassword() + user.getSalt());
        user.setPassword(newPassword);
        int rows = userService.updatePassword(user.getId(), newPassword);
        if (rows <= 0) {
            throw new RuntimeException("【用户更新失败】user = " + gson.toJson(user));
        }

        model.addAttribute("msg", "密码修改成功，请记住您的新密码");
        model.addAttribute("redirectionTime", CommunityConst.AUTO_REDIRECTION_TIME);
        model.addAttribute("target", "/index");
        return "/site/operate-result";
    }
}
