package garry.community.controller;

import com.google.gson.Gson;
import garry.community.annotation.LoginRequired;
import garry.community.consts.CommunityConst;
import garry.community.event.EventProducer;
import garry.community.pojo.Event;
import garry.community.pojo.User;
import garry.community.service.FollowService;
import garry.community.utils.HostHolder;
import garry.community.vo.ResponseVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Garry
 * ---------2024/3/24 16:14
 **/
@Controller
public class FollowController {
    @Resource
    private FollowService followService;

    @Resource
    private HostHolder hostHolder;

    @Resource
    private EventProducer eventProducer;

    /**
     * 接收AJAX的异步请求，添加关注信息到redis，并发送信息到对应消息队列
     *
     * @param entityType
     * @param entityId
     * @return
     */
    @LoginRequired
    @RequestMapping(value = "/follow", method = RequestMethod.POST)
    @ResponseBody
    public String follow(Integer entityType, Integer entityId) {
        User user = hostHolder.getUser();
        followService.follow(user.getId(), entityType, entityId);

        long followerCount = followService.findFollowerCount(entityType, entityId);

        Map<String, Long> map = new HashMap<>();
        map.put("followerCount", followerCount);

        //添加系统通知
        Event event = makeEvent(entityType, entityId);
        eventProducer.fireEvent(event);

        return new Gson().toJson(ResponseVo.success(map));
    }

    /**
     * 传入关注实体和id，取消关注
     *
     * @param entityType
     * @param entityId
     * @return
     */
    @LoginRequired
    @RequestMapping(value = "/unfollow", method = RequestMethod.POST)
    @ResponseBody
    public String unfollow(Integer entityType, Integer entityId) {
        User user = hostHolder.getUser();
        followService.unfollow(user.getId(), entityType, entityId);

        long followerCount = followService.findFollowerCount(entityType, entityId);

        Map<String, Long> map = new HashMap<>();
        map.put("followerCount", followerCount);

        return new Gson().toJson(ResponseVo.success(map));
    }

    /**
     * 获取event
     *
     * @param entityType
     * @param entityId
     * @return
     */
    private Event makeEvent(Integer entityType, Integer entityId) {
        Event event = new Event();
        event.setTopic(CommunityConst.TOPIC_FOLLOW);
        event.setUserId(hostHolder.getUser().getId());
        event.setEntityType(entityType);
        event.setEntityId(entityId);
        event.setEntityUserId(entityId);
        return event;
    }
}
