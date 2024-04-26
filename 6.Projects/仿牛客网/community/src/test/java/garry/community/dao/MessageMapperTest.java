package garry.community.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import garry.community.CommunityApplicationTests;
import garry.community.consts.CommunityConst;
import garry.community.pojo.Message;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Garry
 * ---------2024/3/22 16:30
 **/
@Slf4j
public class MessageMapperTest extends CommunityApplicationTests {
    @Resource
    private MessageMapper messageMapper;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private final Integer UID = 111;

    @Test
    public void selectConversations() {
        List<Message> messages = messageMapper.selectConversations(UID);
        log.info("messages = {}", gson.toJson(messages));
    }

    @Test
    public void selectConversationRows() {
        int rows = messageMapper.selectConversationRows(UID);
        log.info("rows = {}", gson.toJson(rows));
    }

    @Test
    public void selectLetters() {
        List<Message> messages = messageMapper.selectConversations(UID);
        for (Message message : messages) {
            List<Message> messagesInConversation = messageMapper.selectLetters(message.getConversationId());
            log.info("message = {} 的会话下所有的私信为 {}", gson.toJson(message), gson.toJson(messagesInConversation));
        }
    }

    @Test
    public void selectLetterRows() {
        List<Message> messages = messageMapper.selectConversations(UID);
        for (Message message : messages) {
            int rows = messageMapper.selectLetterRows(message.getConversationId());
            log.info("message = {} 的会话下所有的私信总数为 {}", gson.toJson(message), gson.toJson(rows));
        }
    }

    @Test
    public void selectUnreadLettersCount() {
        List<Message> messages = messageMapper.selectConversations(UID);
        int count = messageMapper.selectUnreadLettersCount(UID, null);
        log.info("用户未读消息总数为 {}", count);
        for (Message message : messages) {
            int rows = messageMapper.selectUnreadLettersCount(UID, message.getConversationId());
            log.info("message = {} 的会话下的未读消息总数为 {}", gson.toJson(message), gson.toJson(rows));
        }
    }

    @Test
    public void selectLettersCount() {
        List<Message> messages = messageMapper.selectConversations(UID);
        int count = messageMapper.selectUnreadLettersCount(UID, null);
        log.info("用户消息总数为 {}", count);
        for (Message message : messages) {
            int rows = messageMapper.selectLettersCount(UID, message.getConversationId());
            log.info("message = {} 的会话下的消息总数为 {}", gson.toJson(message), gson.toJson(rows));
        }
    }

    @Test
    public void selectUnreadNoticeCount() {
        int rows = messageMapper.selectUnreadNoticeCount(UID);
        log.info("rows = {}", gson.toJson(rows));
    }

    @Test
    public void selectUnreadSpecificNoticeCount() {
        int sum = 0;
        int rows = messageMapper.selectUnreadSpecificNoticeCount(UID, CommunityConst.TOPIC_COMMENT);
        log.info("rows = {}", gson.toJson(rows));
        sum += rows;
        rows = messageMapper.selectUnreadSpecificNoticeCount(UID, CommunityConst.TOPIC_LIKE);
        log.info("rows = {}", gson.toJson(rows));
        sum += rows;
        rows = messageMapper.selectUnreadSpecificNoticeCount(UID, CommunityConst.TOPIC_FOLLOW);
        log.info("rows = {}", gson.toJson(rows));
        sum += rows;
        log.info("sum = {}", sum);
    }

    @Test
    public void selectSpecificNoticeCount() {
        int sum = 0;
        int rows = messageMapper.selectSpecificNoticeCount(UID, CommunityConst.TOPIC_COMMENT);
        log.info("rows = {}", gson.toJson(rows));
        sum += rows;
        rows = messageMapper.selectSpecificNoticeCount(UID, CommunityConst.TOPIC_LIKE);
        log.info("rows = {}", gson.toJson(rows));
        sum += rows;
        rows = messageMapper.selectSpecificNoticeCount(UID, CommunityConst.TOPIC_FOLLOW);
        log.info("rows = {}", gson.toJson(rows));
        sum += rows;
        log.info("sum = {}", sum);
    }

    @Test
    public void selectRecentMessageOfSpecificNotice() {
        Message message;
        message = messageMapper.selectRecentMessageOfSpecificNotice(UID, CommunityConst.TOPIC_COMMENT);
        log.info("message = {}", gson.toJson(message));
        message = messageMapper.selectRecentMessageOfSpecificNotice(UID, CommunityConst.TOPIC_LIKE);
        log.info("message = {}", gson.toJson(message));
        message = messageMapper.selectRecentMessageOfSpecificNotice(UID, CommunityConst.TOPIC_FOLLOW);
        log.info("message = {}", gson.toJson(message));
        message = messageMapper.selectRecentMessageOfSpecificNotice(UID, null);
        log.info("message = {}", gson.toJson(message));
    }

    @Test
    public void selectSpecificNotice() {
        List<Message> messages;
        messages = messageMapper.selectSpecificNotice(UID, CommunityConst.TOPIC_COMMENT);
        log.info("messages = {}", gson.toJson(messages));
        messages = messageMapper.selectSpecificNotice(UID, CommunityConst.TOPIC_LIKE);
        log.info("messages = {}", gson.toJson(messages));
        messages = messageMapper.selectSpecificNotice(UID, CommunityConst.TOPIC_FOLLOW);
        log.info("messages = {}", gson.toJson(messages));
        messages = messageMapper.selectSpecificNotice(UID, null);
        log.info("messages = {}", gson.toJson(messages));
    }
}