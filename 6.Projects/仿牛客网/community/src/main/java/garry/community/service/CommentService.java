package garry.community.service;

import com.github.pagehelper.PageInfo;
import garry.community.enums.EntityTypeEnum;
import garry.community.pojo.Comment;

import java.util.List;

/**
 * @author Garry
 * ---------2024/3/21 13:08
 **/
public interface CommentService {

    Comment findCommentById(Integer commentId);

    int update(Comment comment);

    /**
     * 根据所属实体，和对应的实体id查询所有非禁用的评论，进行分页，并按时间顺序倒序排列
     *
     * @param entityTypeEnum
     * @param entityId
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<Comment> findComments(EntityTypeEnum entityTypeEnum, Integer entityId, Integer pageNum, Integer pageSize);

    /**
     * 查询某一实体下的评论数量
     *
     * @param entityTypeEnum
     * @param entityId
     * @return
     */
    int findCommentRows(EntityTypeEnum entityTypeEnum, Integer entityId);

    /**
     * 增加评论并过滤，如果评论的实体是帖子，则帖子总数增加
     *
     * @param comment
     * @return
     */
    int addComment(Comment comment);

    /**
     * 根据userId查询用户的所有评论的总数；
     * 如果userId=null或userId=0，则返回所有评论的总数
     *
     * @param userId
     * @return
     */
    int findCommentCountByUserId(Integer userId);

    /**
     * 根据userId查询用户的所有评论(按时间倒序、并进行分页)；
     * 如果userId=null或userId=0，则返回所有评论
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<Comment> findCommentsByUserId(Integer userId, Integer pageNum, Integer pageSize);

    List<Comment> findCommentByEntityId(Integer entityId);
}
