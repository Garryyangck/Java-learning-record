package garry.community.dao.elasticsearch;

import com.google.gson.GsonBuilder;
import garry.community.CommunityApplicationTests;
import garry.community.dao.DiscussPostMapper;
import garry.community.pojo.DiscussPost;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Garry
 * ---------2024/3/27 19:56
 **/
@Slf4j
public class DiscussPostRepositoryTest extends CommunityApplicationTests {
    @Resource
    private DiscussPostMapper discussPostMapper;

    @Resource
    private DiscussPostRepository discussPostRepository;

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 添加一个数据
     */
    @Test
    public void testInsert() {
        discussPostRepository.save(discussPostMapper.selectByPrimaryKey(275));
        discussPostRepository.save(discussPostMapper.selectByPrimaryKey(274));
    }

    /**
     * 添加多个数据
     */
    @Test
    public void testInsertList() {
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(101, false));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(102, false));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(103, false));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(111, false));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(112, false));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(131, false));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(132, false));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(133, false));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(134, false));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(11, false));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(176, false));
    }

    /**
     * 删除
     */
    @Test
    public void testDelete() {
        discussPostRepository.deleteById(231);
        discussPostRepository.deleteAll();
    }

    /**
     * 查询
     */
    @Test
    public void testSearch() {
        /*
            使用discussPostRepository(继承ElasticsearchRepository)的方法无法获取高亮的文本，底层的设计缺陷
         */
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery("互联网寒冬", "title", "content"))
                .withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withPageable(PageRequest.of(0, 10))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                ).build();
        Page<DiscussPost> page = discussPostRepository.search(searchQuery);
        System.out.println(page.getTotalPages());
        System.out.println(page.getTotalElements());
        System.out.println(page.getNumber());
        System.out.println(page.getSize());
        for (DiscussPost discussPost : page) {
            log.info("post = {}", new GsonBuilder().setPrettyPrinting().create().toJson(discussPost));
        }
    }

    /**
     * 使用elasticsearchTemplate查询，解决查询结果没有高亮显示的问题
     */
    @Test
    public void testSearchByTemplate() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery("互联网寒冬", "title", "content"))
                .withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withPageable(PageRequest.of(0, 10))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                ).build();

        AggregatedPage<DiscussPost> page = elasticsearchTemplate.queryForPage(searchQuery, DiscussPost.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> aClass, Pageable pageable) {
                SearchHits hits = response.getHits();
                if (hits.getTotalHits() <= 0) {
                    return null;
                }

                List<DiscussPost> list = new ArrayList<>();
                //遍历命中结果
                for (SearchHit hit : hits) {
                    DiscussPost post = new DiscussPost();

                    String id = hit.getSourceAsMap().get("id").toString();
                    post.setId(Integer.valueOf(id));

                    String userId = hit.getSourceAsMap().get("userId").toString();
                    post.setUserId(Integer.valueOf(userId));

                    String title = hit.getSourceAsMap().get("title").toString();
                    post.setTitle(title);

                    String content = hit.getSourceAsMap().get("content").toString();
                    post.setContent(content);

                    String type = hit.getSourceAsMap().get("type").toString();
                    post.setType(Integer.valueOf(type));

                    String status = hit.getSourceAsMap().get("status").toString();
                    post.setStatus(Integer.valueOf(status));

                    String createTime = hit.getSourceAsMap().get("createTime").toString();
                    post.setCreateTime(new Date(Long.valueOf(createTime)));

                    String commentCount = hit.getSourceAsMap().get("commentCount").toString();
                    post.setCommentCount(Integer.valueOf(commentCount));

                    String score = hit.getSourceAsMap().get("score").toString();
                    post.setScore(Double.valueOf(score));

                    HighlightField titleField = hit.getHighlightFields().get("title");
                    if (titleField != null) {
                        Text[] fragments = titleField.getFragments();
                        post.setTitle(titleField.getFragments()[0].toString());
                    }
                    HighlightField contentField = hit.getHighlightFields().get("content");
                    if (contentField != null) {
                        Text[] fragments = contentField.getFragments();
                        post.setContent(contentField.getFragments()[0].toString());
                    }

                    list.add(post);
                }

                return new AggregatedPageImpl(list, pageable,
                        hits.getTotalHits(), response.getAggregations(),
                        response.getScrollId(), hits.getMaxScore());
            }
        });

        System.out.println(page.getTotalPages());
        System.out.println(page.getTotalElements());
        System.out.println(page.getNumber());//页码
        System.out.println(page.getSize());
        for (DiscussPost discussPost : page) {
            log.info("post = {}", new GsonBuilder().setPrettyPrinting().create().toJson(discussPost));
        }
    }
}