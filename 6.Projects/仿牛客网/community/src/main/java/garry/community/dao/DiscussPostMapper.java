package garry.community.dao;

import garry.community.pojo.DiscussPost;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DiscussPostMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DiscussPost record);

    int insertSelective(DiscussPost record);

    DiscussPost selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DiscussPost record);

    int updateByPrimaryKey(DiscussPost record);

    int deleteByUserId(@Param(value = "userId") Integer userId);

    /**
     * 查询用户的所有帖子
     * userId = 0即查询所有非拉黑帖子
     * 排序方式：优先置顶贴(onTop=true时才会优先置顶贴)，然后优先最近发布时间
     *
     * @param userId
     * @param onTop
     * @return
     */
    List<DiscussPost> selectDiscussPosts(@Param(value = "userId") Integer userId, @Param(value = "onTop") Boolean onTop);

    /**
     * 优先按照分数而非时间排序
     *
     * @param userId
     * @param onTop
     * @return
     */
    List<DiscussPost> selectHotDiscussPosts(@Param(value = "userId") Integer userId, @Param(value = "onTop") Boolean onTop);

    /**
     * 查询用户帖子的数量
     * userId = 0即统计所有非拉黑帖子
     *
     * @param userId
     * @return
     */
    int selectDiscussPostRows(@Param(value = "userId") Integer userId);

    /**
     * 查找所有非拉黑的帖子
     *
     * @return
     */
    List<DiscussPost> selectAllPosts();
}