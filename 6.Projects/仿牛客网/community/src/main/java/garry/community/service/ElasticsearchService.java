package garry.community.service;

import garry.community.pojo.DiscussPost;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;

/**
 * @author Garry
 * ---------2024/3/28 9:38
 **/
public interface ElasticsearchService {
    /**
     * 搜索帖子(注意：返回的页码从0开始)
     *
     * @param searchWord
     * @param pageNum    传入的page从1开始，但是处理page时需要从0开始
     * @param pageSize
     * @return
     */
    AggregatedPage<DiscussPost> searchPost(String searchWord, Integer pageNum, Integer pageSize);

    /**
     * 将新增的帖子加入到ES的节点中
     *
     * @param discussPost
     */
    void addPost(DiscussPost discussPost);

    /**
     * 删除ES对应节点中id为postId的帖子
     *
     * @param postId
     */
    void deletePost(Integer postId);
}
