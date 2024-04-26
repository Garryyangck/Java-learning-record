package garry.community.dao;

import garry.community.pojo.Message;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Message record);

    int insertSelective(Message record);

    Message selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Message record);

    int updateByPrimaryKey(Message record);

    /**
     * 获取当前用户的会话列表(不包含系统消息)，针对每一个会话只返回最新的一条消息，
     * 并使用pageHelper进行分页处理，并按时间倒序
     *
     * @param userId
     * @return
     */
    List<Message> selectConversations(@Param(value = "userId") Integer userId);

    /**
     * 获取当前用户的会话总数(不包含系统消息)
     *
     * @param userId
     * @return
     */
    int selectConversationRows(@Param(value = "userId") Integer userId);

    /**
     * 获取某一个会话下的所有私信，并进行分页
     *
     * @param conversationId
     * @return
     */
    List<Message> selectLetters(@Param(value = "conversationId") String conversationId);

    /**
     * 获取某一个会话的私信总数
     *
     * @param conversationId
     * @return
     */
    int selectLetterRows(@Param(value = "conversationId") String conversationId);

    /**
     * 查询用户某一会话中未读私信总数(不包含系统消息)
     * 如果conversationId=null，则查询所有会话未读私信总数
     *
     * @param userId
     * @param conversationId
     * @return
     */
    int selectUnreadLettersCount(@Param(value = "userId") Integer userId,
                                 @Param(value = "conversationId") String conversationId);

    /**
     * 查询用户某一会话中私信总数(不包含系统消息)
     * 如果conversationId=null，则查询所有会话私信总数
     *
     * @param userId
     * @param conversationId
     * @return
     */
    int selectLettersCount(@Param(value = "userId") Integer userId,
                           @Param(value = "conversationId") String conversationId);

    /**
     * 查询某用户当前未读系统消息总数；
     * userId为null，则返回所有未读消息总数
     *
     * @param userId
     * @return
     */
    int selectUnreadNoticeCount(@Param(value = "userId") Integer userId);

    /**
     * 查询某用户某一特定系统消息的未读总数；
     * userId为null，则返回某一特定系统消息所有的未读总数
     *
     * @param userId
     * @param noticeTopic
     * @return
     */
    int selectUnreadSpecificNoticeCount(@Param(value = "userId") Integer userId, @Param(value = "conversationId") String noticeTopic);

    /**
     * 查询某用户某一特定系统消息的最近消息；
     * 如果没有该类系统消息，则返回null；
     * 如果noticeTopic为null，则会返回所有系统消息中最近的消息；
     * userId为null，则返回某一特定系统消息的最近消息
     *
     * @param userId
     * @param noticeTopic
     * @return
     */
    Message selectRecentMessageOfSpecificNotice(@Param(value = "userId") Integer userId, @Param(value = "conversationId") String noticeTopic);

    /**
     * 查询某一特定系统消息的总数；
     * 如果noticeTopic为null，则会返回用户所有系统消息的总数；
     * userId为null，则返回某一特定系统消息的总数
     *
     * @param userId
     * @param noticeTopic
     * @return
     */
    int selectSpecificNoticeCount(@Param(value = "userId") Integer userId, @Param(value = "conversationId") String noticeTopic);

    /**
     * 查询某用户某一特定系统消息下的所有消息(优先未读消息，然后按照时间倒序排序)；
     * 如果noticeTopic为null，则会返回所有系统消息；
     * userId为null，则返回某一特定系统消息下的所有消息
     *
     * @param userId
     * @param noticeTopic
     * @return
     */
    List<Message> selectSpecificNotice(@Param(value = "userId") Integer userId, @Param(value = "conversationId") String noticeTopic);
}