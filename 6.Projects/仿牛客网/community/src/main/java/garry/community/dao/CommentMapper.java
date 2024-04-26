package garry.community.dao;

import garry.community.pojo.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    /**
     * 查询某一实体下所有非禁用评论，并按时间升序排列
     *
     * @param entityType
     * @param entityId
     * @return
     */
    List<Comment> selectComments(@Param(value = "entityType") Integer entityType, @Param(value = "entityId") Integer entityId);

    /**
     * 查询某一实体下所有非禁用评论的数量
     *
     * @param entityType
     * @param entityId
     * @return
     */
    int selectCommentRows(@Param(value = "entityType") Integer entityType, @Param(value = "entityId") Integer entityId);

    /**
     * 根据userId查询用户的所有评论的总数；
     * 如果userId=null或userId=0，则返回所有评论的总数
     *
     * @param userId
     * @return
     */
    int selectCommentCountByUserId(@Param(value = "userId") Integer userId);

    /**
     * 根据userId查询用户的所有评论(按时间倒序)；
     * 如果userId=null或userId=0，则返回所有评论
     *
     * @param userId
     * @return
     */
    List<Comment> selectCommentByUserId(@Param(value = "userId") Integer userId);

    List<Comment> selectCommentByEntityId(@Param(value = "entityId") Integer entityId);
}