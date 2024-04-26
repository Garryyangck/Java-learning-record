package garry.community.controller;

import com.github.pagehelper.PageInfo;
import garry.community.consts.CommunityConst;
import garry.community.enums.EntityTypeEnum;
import garry.community.pojo.DiscussPost;
import garry.community.pojo.User;
import garry.community.service.DiscussPostService;
import garry.community.service.LikeService;
import garry.community.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Garry
 * ---------2024/3/15 20:40
 **/
@Controller
public class HomeController {

    @Resource
    private DiscussPostService discussPostService;

    @Resource
    private UserService userService;

    @Resource
    private LikeService likeService;

    /**
     * 显示论坛主页，可以显示特定用户的帖子，并支持分页
     *
     * @param model
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param type     type=0按时间倒序，type=1按分数倒序
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String getIndexPage(Model model,/*渲染模型*/
                               @RequestParam(value = "userId", required = false, defaultValue = "0") Integer userId,
                               @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                               @RequestParam(value = "type", required = false, defaultValue = "0") Integer type) {

        //查询userId用户的所有帖子，并借助Mybatis-pageHelper进行分页，得到分页详情
        PageInfo<DiscussPost> pageInfo = null;
        if (type.equals(0)) {
            pageInfo = discussPostService.findDiscussPosts(userId, pageNum, pageSize, true);
        } else if (type.equals(1)) {
            pageInfo = discussPostService.findHotDiscussPosts(userId, pageNum, pageSize, true);
        }
        if (pageInfo == null) {
            return "/error/404";
        }
        //得到查到的discussPostList
        List<DiscussPost> discussPostList = pageInfo.getList();
        //stream+lambda，List<DiscussPost> =>  List<HashMap<String, Object>>
        //以便前端可以直接获取帖子的user(用户详情)和post(帖子详情)
        List<HashMap<String, Object>> discussPosts = discussPostList.stream()
                .map(discussPost -> {
                    HashMap<String, Object> map = new HashMap<>();
                    User user = userService.findByUserId(discussPost.getUserId());
                    //获取每一个帖子的点赞总数
                    long likeCount = likeService
                            .findEntityLikeCount(EntityTypeEnum.DISCUSS_POST.getCode(), discussPost.getId());

                    map.put("user", user);
                    map.put("post", discussPost);
                    map.put("likeCount", likeCount);
                    return map;
                }).collect(Collectors.toList());

        //向模板中写入discussPosts，用于遍历显示帖子
        model.addAttribute("discussPosts", discussPosts);
        //传入种类，用于正确显示页码
        model.addAttribute("type", type);
        //向模板中写入pageInfo，用于控制分页
        model.addAttribute("pageInfo", pageInfo);
        //写入分页的页面，便于分页模板的复用
        model.addAttribute("pagePath", "/index");
        //传入userId，以便前端分页跳转时，可以跳到正确的user的页面
        model.addAttribute("userId", userId);
        //传入分页导航栏每一边的长度
        model.addAttribute("navigatePageWidth", CommunityConst.NAVIGATE_PAGE_WIDTH);
        return "/index";//不用写.html
    }

    /**
     * 用于统一异常管理，进行重定向
     *
     * @return
     */
    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String getErrorPage() {
        return "/error/500";
    }

    /**
     * 用户权限不足时，返回404页面
     *
     * @return
     */
    @RequestMapping(value = "/denied", method = RequestMethod.GET)
    public String getDeniedPage() {
        return "/error/404";
    }
}
