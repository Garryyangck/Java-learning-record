package garry.community.controller;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import garry.community.annotation.LoginRequired;
import garry.community.consts.CommunityConst;
import garry.community.enums.MessageStatusEnum;
import garry.community.enums.ResponseEnum;
import garry.community.form.AddMessageLetterForm;
import garry.community.pojo.Message;
import garry.community.pojo.User;
import garry.community.service.MessageService;
import garry.community.service.UserService;
import garry.community.utils.HostHolder;
import garry.community.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Garry
 * ---------2024/3/22 15:34
 **/
@Slf4j
@Controller
@RequestMapping(value = "/message")
public class MessageController {
    @Resource
    private MessageService messageService;

    @Resource
    private UserService userService;

    @Resource
    private HostHolder hostHolder;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * 获取朋友私信页面
     *
     * @param model
     * @param pageNum
     * @param pageSize
     * @return
     */
    @LoginRequired
    @RequestMapping(value = "/letter/list", method = RequestMethod.GET)
    public String getLetterPage(Model model,
                                @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        User user = hostHolder.getUser();

        //查询会话，每一个会话只显示最新的私信
        PageInfo<Message> pageInfo = messageService.findConversations(user.getId(), pageNum, pageSize);
        List<Message> conversationList = pageInfo.getList();

        //丰富conversation所需信息
        List<Map<String, Object>> conversations = conversationList.stream()
                .map(conversation -> {
                    Map<String, Object> map = new HashMap<>();
                    //写入该会话未读私信数
                    int unreadLettersCount = messageService.findUnreadLettersCount(user.getId(), conversation.getConversationId());
                    map.put("unreadLettersCount", unreadLettersCount);
                    //写入该会话总私信数
                    int lettersCount = messageService.findLettersCount(user.getId(), conversation.getConversationId());
                    map.put("lettersCount", lettersCount);
                    //写入该会话对方的user信息
                    User friend = new User();
                    if (user.getId().equals(conversation.getFromId())) {
                        friend = userService.findByUserId(conversation.getToId());
                    } else if (user.getId().equals(conversation.getToId())) {
                        friend = userService.findByUserId(conversation.getFromId());
                    }
                    map.put("friend", friend);
                    //写入当前会话第一条消息的详情
                    map.put("firstMessage", conversation);
                    return map;
                }).sorted((conversation1, conversation2) -> //将conversations按未读私信总数排序
                        (Integer) conversation2.get("unreadLettersCount") -
                                (Integer) conversation1.get("unreadLettersCount")).collect(Collectors.toList());

        //查询用户未读私信总数
        int unreadLettersCount = messageService.findUnreadLettersCount(user.getId(), null);

        //查询用户未读系统通知总数
        int unreadNoticeCount = messageService.findUnreadNoticeCount(user.getId());

        //向模板中写入conversations，用于遍历显示
        model.addAttribute("conversations", conversations);
        //传入用户未读私信总数
        model.addAttribute("unreadLettersCount", unreadLettersCount);
        //传入用户未读系统通知总数
        model.addAttribute("unreadNoticeCount", unreadNoticeCount);
        //向模板中写入pageInfo，用于控制分页
        model.addAttribute("pageInfo", pageInfo);
        //写入分页的页面，便于分页模板的复用
        model.addAttribute("pagePath", "/message/letter/list");
        //传入分页导航栏每一边的长度
        model.addAttribute("navigatePageWidth", CommunityConst.NAVIGATE_PAGE_WIDTH);
        return "/site/letter";
    }

    /**
     * 获取会话详情页面
     *
     * @param model
     * @param pageNum
     * @param pageSize
     * @return
     */
    @LoginRequired
    @RequestMapping(value = "/letter/detail", method = RequestMethod.GET)
    public String getLetterDetailPage(Model model,
                                      Integer friendId,
                                      String conversationId,
                                      Integer fromPageNum,
                                      @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        User friend = userService.findByUserId(friendId);
        if (friend == null) {
            throw new RuntimeException("【friend为null】");
        }

        PageInfo<Message> pageInfo = messageService.findLetters(conversationId, pageNum, pageSize);
        List<Message> messageList = pageInfo.getList();

        List<Map<String, Object>> messages = messageList.stream()
                .map(message -> {
                    Map<String, Object> map = new HashMap<>();
                    User fromUser = userService.findByUserId(message.getFromId());
                    //进入之前的状态(要么是0，要么是1，2不会查出来)
                    Integer preStatus = message.getStatus();
                    //user是message的接收人
                    if (message.getToId().equals(hostHolder.getUser().getId())) {
                        //状态改为已读
                        message.setStatus(MessageStatusEnum.READ.getCode());
                        //写入数据库
                        messageService.update(message);
                    } else {//user是message的发起人，则不要在letter-detail中显示未读
                        preStatus = 1;
                    }
                    map.put("user", fromUser);
                    map.put("preStatus", preStatus);
                    map.put("message", message);
                    return map;
                }).collect(Collectors.toList());

        //向模板中写入messages，用于遍历显示
        model.addAttribute("messages", messages);
        //写入friend，用于显示
        model.addAttribute("friend", friend);
        //写入conversationId，注意get请求中，前端页面无法获取参数中的值
        model.addAttribute("conversationId", conversationId);
        //传入fromPageNum，用于详情页返回对应list的那一页
        model.addAttribute("fromPageNum", fromPageNum);
        //向模板中写入pageInfo，用于控制分页
        model.addAttribute("pageInfo", pageInfo);
        //写入分页的页面，便于分页模板的复用
        model.addAttribute("pagePath", "/message/letter/detail");
        //传入分页导航栏每一边的长度
        model.addAttribute("navigatePageWidth", CommunityConst.NAVIGATE_PAGE_WIDTH);

        return "/site/letter-detail";
    }

    /**
     * 显示系统通知列表页
     *
     * @param model
     * @return
     */
    @LoginRequired
    @RequestMapping(value = "/notice/list", method = RequestMethod.GET)
    public String getNoticePage(Model model) {
        User user = hostHolder.getUser();

        //传入未读私信和未读系统消息总数
        int unreadLettersCount = messageService.findUnreadLettersCount(user.getId(), null);
        model.addAttribute("unreadLettersCount", unreadLettersCount);
        int unreadNoticeCount = messageService.findUnreadNoticeCount(user.getId());
        model.addAttribute("unreadNoticeCount", unreadNoticeCount);

        //传入评论、点赞、关注未读消息数
        int unreadCommentCount = messageService.findUnreadSpecificNoticeCount(user.getId(), CommunityConst.TOPIC_COMMENT);
        model.addAttribute("unreadCommentCount", unreadCommentCount);
        int unreadLikeCount = messageService.findUnreadSpecificNoticeCount(user.getId(), CommunityConst.TOPIC_LIKE);
        model.addAttribute("unreadLikeCount", unreadLikeCount);
        int unreadFollowCount = messageService.findUnreadSpecificNoticeCount(user.getId(), CommunityConst.TOPIC_FOLLOW);
        model.addAttribute("unreadFollowCount", unreadFollowCount);

        //传入每种类型的最近消息(注意：考虑没有消息导致的空指针问题)
        Message firstComment = messageService.findRecentMessageOfSpecificNotice(user.getId(), CommunityConst.TOPIC_COMMENT);
        model.addAttribute("firstComment", firstComment);
        Double userId;
        Double entityType;
        if (firstComment != null) {
            userId = (Double) messageService.getMapFromContent(firstComment.getContent()).get("userId");
            entityType = (Double) messageService.getMapFromContent(firstComment.getContent()).get("entityType");
            model.addAttribute("commentUser", userService.findByUserId(userId.intValue()));
            model.addAttribute("commentEntityType", entityType.intValue());
        }
        Message firstLike = messageService.findRecentMessageOfSpecificNotice(user.getId(), CommunityConst.TOPIC_LIKE);
        model.addAttribute("firstLike", firstLike);
        if (firstLike != null) {
            userId = (Double) messageService.getMapFromContent(firstLike.getContent()).get("userId");
            entityType = (Double) messageService.getMapFromContent(firstLike.getContent()).get("entityType");
            model.addAttribute("likeUser", userService.findByUserId(userId.intValue()));
            model.addAttribute("likeEntityType", entityType.intValue());
        }
        Message firstFollow = messageService.findRecentMessageOfSpecificNotice(user.getId(), CommunityConst.TOPIC_FOLLOW);
        model.addAttribute("firstFollow", firstFollow);
        if (firstFollow != null) {
            userId = (Double) messageService.getMapFromContent(firstFollow.getContent()).get("userId");
            entityType = (Double) messageService.getMapFromContent(firstFollow.getContent()).get("entityType");
            model.addAttribute("followUser", userService.findByUserId(userId.intValue()));
            model.addAttribute("followEntityType", entityType.intValue());
        }

        //传入每种类型的消息总数
        int commentCount = messageService.findSpecificNoticeCount(user.getId(), CommunityConst.TOPIC_COMMENT);
        model.addAttribute("commentCount", commentCount);
        int likeCount = messageService.findSpecificNoticeCount(user.getId(), CommunityConst.TOPIC_LIKE);
        model.addAttribute("likeCount", likeCount);
        int followCount = messageService.findSpecificNoticeCount(user.getId(), CommunityConst.TOPIC_FOLLOW);
        model.addAttribute("followCount", followCount);

        return "/site/notice";
    }

    /**
     * 显示系统通知的详情页
     *
     * @param model
     * @param noticeTopic
     * @param pageNum
     * @param pageSize
     * @return
     */
    @LoginRequired
    @RequestMapping(value = "/notice/detail", method = RequestMethod.GET)
    public String getNoticeDetailPage(Model model,
                                      String noticeTopic,
                                      @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        User user = hostHolder.getUser();

        PageInfo<Message> pageInfo = messageService.findSpecificNotice(user.getId(), noticeTopic, pageNum, pageSize);
        List<Message> messageList = pageInfo.getList();
        List<Map<String, Object>> messages = messageList.stream()
                .map(message -> {
                    Map<String, Object> map = new HashMap<>();
                    Map<String, Object> mapFromContent = messageService.getMapFromContent(message.getContent());
                    Double userId = (Double) mapFromContent.get("userId");
                    User messageUser = userService.findByUserId(userId.intValue());
                    map.put("message", message);
                    map.put("user", messageUser);
                    //传入用户阅读消息将其修改为已读之前的消息状态
                    map.put("preStatus", message.getStatus());
                    //状态改为已读
                    message.setStatus(MessageStatusEnum.READ.getCode());
                    //写入数据库
                    messageService.update(message);
                    //传入实体类型，用于前端动态显示不同实体
                    Double entityType = (Double) mapFromContent.get("entityType");
                    map.put("entityType", entityType.intValue());
                    //如果找得到postId，则传入postId
                    Double postId = (Double) mapFromContent.get("postId");
                    if (postId != null) {
                        map.put("postId", postId.intValue());
                    } else {
                        map.put("postId", null);
                    }
                    return map;
                }).collect(Collectors.toList());

        //向模板中写入messages，用于遍历显示系统消息
        model.addAttribute("messages", messages);
        //向模板中写入pageInfo，用于控制分页
        model.addAttribute("pageInfo", pageInfo);
        //写入分页的页面，便于分页模板的复用
        model.addAttribute("pagePath", "/message/notice/detail");
        //传入noticeTopic，以便前端分页跳转时，可以跳到正确的noticeTopic的页面
        model.addAttribute("noticeTopic", noticeTopic);
        //传入分页导航栏每一边的长度
        model.addAttribute("navigatePageWidth", CommunityConst.NAVIGATE_PAGE_WIDTH);

        return "/site/notice-detail";
    }

    /**
     * 发送私信
     *
     * @param addMessageLetterForm
     * @return
     */
    @LoginRequired
    @RequestMapping(value = "/letter/send", method = RequestMethod.POST)
    @ResponseBody
    public String addMessageLetter(AddMessageLetterForm addMessageLetterForm) {
        if (addMessageLetterForm == null) {
            throw new IllegalArgumentException("【addMessageLetterForm为null】");
        }

        Message message = makeMessageLetter(addMessageLetterForm);
        if (message == null) {
            return gson.toJson(ResponseVo.error(ResponseEnum.TO_USER_NOT_EXIST));
        }

        //添加message
        int rows = messageService.addMessage(message);
        if (rows <= 0) {
            throw new RuntimeException("【更新失败】message = " + gson.toJson(message));
        }

        return gson.toJson(ResponseVo.success());
    }

    /**
     * 删除私信或系统消息(状态改为)
     *
     * @param messageId
     * @return
     */
    @LoginRequired
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String deleteMessage(Integer messageId) {
        if (messageId == null) {
            throw new IllegalArgumentException("【参数为null】");
        }

        Message message = messageService.findById(messageId);
        message.setStatus(MessageStatusEnum.DELETE.getCode());
        int rows = messageService.update(message);
        if (rows <= 0) {
            throw new RuntimeException("【更新失败】message = " + gson.toJson(message));
        }

        return gson.toJson(ResponseVo.success());
    }

    /**
     * 根据addMessageLetterForm创建MessageLetter
     *
     * @param addMessageLetterForm
     * @return
     */
    private Message makeMessageLetter(AddMessageLetterForm addMessageLetterForm) {
        Message message = new Message();
        Integer fromId = hostHolder.getUser().getId();
        Integer toId;
        try {
            toId = userService.findByUsername(addMessageLetterForm.getToUsername()).getId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        String conversationId = Math.min(fromId, toId) + "_" + Math.max(fromId, toId);
        message.setFromId(fromId);
        message.setToId(toId);
        message.setConversationId(conversationId);
        message.setContent(addMessageLetterForm.getContent());
        message.setStatus(MessageStatusEnum.UNREAD.getCode());
        message.setCreateTime(new Date());
        return message;
    }
}
