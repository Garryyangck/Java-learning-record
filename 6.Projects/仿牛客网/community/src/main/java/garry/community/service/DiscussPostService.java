package garry.community.service;

import com.github.pagehelper.PageInfo;
import garry.community.pojo.DiscussPost;

/**
 * @author Garry
 * ---------2024/3/15 20:19
 **/
public interface DiscussPostService {

    DiscussPost findPostById(Integer id);

    int update(DiscussPost post);

    /**
     * 查询某一用户非拉黑的帖子，并进行分页，并将置顶帖子放在最前面(onTop=true)
     * userId = 0会查出所有非拉黑帖子
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param onTop
     * @return
     */
    PageInfo<DiscussPost> findDiscussPosts(Integer userId, Integer pageNum, Integer pageSize, Boolean onTop);

    /**
     * 查询最热的帖子，即先置顶，然后按分数而不是时间倒序
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param onTop
     * @return
     */
    PageInfo<DiscussPost> findHotDiscussPosts(Integer userId, Integer pageNum, Integer pageSize, Boolean onTop);

    /**
     * 查询某一用户非拉黑帖子数量
     * userId = 0查出所有非拉黑帖子数量
     *
     * @param userId
     * @return
     */
    int findDiscussPostRows(Integer userId);

    /**
     * 添加帖子，并过滤敏感词
     *
     * @param discussPost
     * @return
     */
    int addDiscussPost(DiscussPost discussPost);
}
