package garry.community.service;

import com.github.pagehelper.PageInfo;
import garry.community.pojo.Message;

import java.util.Map;

/**
 * @author Garry
 * ---------2024/3/22 15:40
 **/
public interface MessageService {

    Message findById(Integer messageId);

    int update(Message message);

    /**
     * 查询当前用户的所有会话，每个会话只返回最新的一条消息用于显示，
     * 按时间倒序，并进行分页处理
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<Message> findConversations(Integer userId, Integer pageNum, Integer pageSize);

    /**
     * 查询该conversationId下的所有私信，按时间倒序，并进行分页处理
     *
     * @param conversationId
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<Message> findLetters(String conversationId, Integer pageNum, Integer pageSize);

    /**
     * 查询用户某一会话下未读消息的总数，如果conversationId=null，
     * 则查询用户所有未读消息总数
     *
     * @param userId
     * @param conversationId
     * @return
     */
    int findUnreadLettersCount(Integer userId, String conversationId);

    /**
     * 查询用户某一会话下私信的总数，如果conversationId=null，
     * 则查询用户所有消息总数
     *
     * @param userId
     * @param conversationId
     * @return
     */
    int findLettersCount(Integer userId, String conversationId);

    int addMessage(Message message);

    /**
     * 查询某用户当前未读系统消息总数；
     * userId为null，则返回所有未读消息总数
     *
     * @param userId
     * @return
     */
    int findUnreadNoticeCount(Integer userId);

    /**
     * 查询某用户某一特定系统消息的未读总数；
     * userId为null，则返回某一特定系统消息所有的未读总数
     *
     * @param userId
     * @param noticeTopic
     * @return
     */
    int findUnreadSpecificNoticeCount(Integer userId, String noticeTopic);

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
    Message findRecentMessageOfSpecificNotice(Integer userId, String noticeTopic);

    /**
     * 查询某一特定系统消息的总数；
     * 如果noticeTopic为null，则会返回用户所有系统消息的总数；
     * userId为null，则返回某一特定系统消息的总数
     *
     * @param userId
     * @param noticeTopic
     * @return
     */
    int findSpecificNoticeCount(Integer userId, String noticeTopic);

    /**
     * 查询某用户某一特定系统消息下的所有消息(优先未读消息，然后按照时间倒序排序)；
     * 如果noticeTopic为null，则会返回所有系统消息；
     * userId为null，则返回某一特定系统消息下的所有消息；
     * 支持分页功能
     *
     * @param userId
     * @param noticeTopic
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<Message> findSpecificNotice(Integer userId, String noticeTopic, Integer pageNum, Integer pageSize);

    /**
     * 将系统消息的json content还原为Map
     *
     * @param content
     * @return
     */
    Map<String, Object> getMapFromContent(String content);
}
