package garry.community.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import garry.community.CommunityApplicationTests;
import garry.community.pojo.DiscussPost;
import garry.community.pojo.Page;
import garry.community.service.ElasticsearchService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Garry
 * ---------2024/3/28 9:57
 **/
@Slf4j
public class ElasticsearchServiceImplTest extends CommunityApplicationTests {
    @Resource
    private ElasticsearchService elasticsearchService;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void searchPost() {
        Integer pageNum = 1;
        AggregatedPage<DiscussPost> page = elasticsearchService.searchPost("管理员", pageNum, 10);

        List<DiscussPost> discussPostList = page.getContent();

        Page pageInfo = new Page();
        pageInfo.setPages(page.getTotalPages());
        pageInfo.setPageNum(pageNum);
        pageInfo.setPrePage(pageNum - 1);
        pageInfo.setNextPage(pageNum + 1);
        log.info("pageInfo = {}", gson.toJson(pageInfo));

        for (DiscussPost discussPost : discussPostList) {
            log.info("discussPost = {}", gson.toJson(discussPost));
        }
    }
}