package garry.community.controller;

import garry.community.consts.CommunityConst;
import garry.community.enums.EntityTypeEnum;
import garry.community.pojo.DiscussPost;
import garry.community.pojo.Page;
import garry.community.pojo.User;
import garry.community.service.ElasticsearchService;
import garry.community.service.LikeService;
import garry.community.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Garry
 * ---------2024/3/28 11:51
 **/
@Controller
public class SearchController {
    @Resource
    private UserService userService;

    @Resource
    private LikeService likeService;

    @Resource
    private ElasticsearchService elasticsearchService;

    /**
     * 跳转到搜索结果页面
     *
     * @param model
     * @param searchWord
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String getSearchPage(Model model, String searchWord,
                                @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        if (StringUtils.isBlank(searchWord)) {
            model.addAttribute("msg", "请检查您的输入是否为空");
            model.addAttribute("target", "/index");
            model.addAttribute("redirectionTime", CommunityConst.AUTO_REDIRECTION_TIME);
            return "/site/operate-result";
        }

        AggregatedPage<DiscussPost> page = elasticsearchService.searchPost(searchWord, pageNum, pageSize);

        Page pageInfo = new Page();
        List<HashMap<String, Object>> discussPosts = new ArrayList<>();
        //没有搜到任何内容，page==null
        if (page == null) {
            pageInfo.setPages(0);
        } else {
            pageInfo.setPages(page.getTotalPages());
            pageInfo.setPageNum(pageNum);
            pageInfo.setPrePage(pageNum - 1);
            pageInfo.setNextPage(pageNum + 1);

            List<DiscussPost> discussPostList = page.getContent();
            discussPosts = discussPostList.stream()
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
        }

        //向模板中写入discussPosts，用于遍历显示帖子
        model.addAttribute("discussPosts", discussPosts);
        //向模板中写入pageInfo，用于控制分页
        model.addAttribute("pageInfo", pageInfo);
        //写入分页的页面，便于分页模板的复用
        model.addAttribute("pagePath", "/search");
        //传入searchWord，以便前端分页跳转时，可以跳到正确的searchWord的页面
        model.addAttribute("searchWord", searchWord);
        //传入分页导航栏每一边的长度
        model.addAttribute("navigatePageWidth", CommunityConst.NAVIGATE_PAGE_WIDTH);

        return "/site/search";
    }
}
