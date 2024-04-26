package garry.community.controller;

import com.google.gson.Gson;
import garry.community.annotation.LoginRequired;
import garry.community.consts.CommunityConst;
import garry.community.enums.EntityTypeEnum;
import garry.community.event.EventProducer;
import garry.community.form.LikeForm;
import garry.community.pojo.Event;
import garry.community.pojo.User;
import garry.community.service.LikeService;
import garry.community.utils.HostHolder;
import garry.community.utils.RedisKeyUtil;
import garry.community.vo.LikeVo;
import garry.community.vo.ResponseVo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author Garry
 * ---------2024/3/23 22:13
 **/
@Controller
public class LikeController {
    @Resource
    private LikeService likeService;

    @Resource
    private HostHolder hostHolder;

    @Resource
    private EventProducer eventProducer;

    @Resource
    private RedisTemplate redisTemplate;

    private final Gson gson = new Gson();

    /**
     * 接收AJAX的异步请求，向redis中添加点赞信息，并发送消息至对应消息队列
     *
     * @param likeForm
     * @return
     */
    @LoginRequired
    @RequestMapping(value = "/like", method = RequestMethod.POST)
    @ResponseBody
    public String like(LikeForm likeForm) {
        User user = hostHolder.getUser();
        likeService.like(user.getId(), likeForm.getEntityType(), likeForm.getEntityId(), likeForm.getReceiverId());

        long likeCount = likeService.findEntityLikeCount(likeForm.getEntityType(), likeForm.getEntityId());
        int likeStatus = likeService.findEntityLikeStatus(user.getId(), likeForm.getEntityType(), likeForm.getEntityId());

        LikeVo likeVo = new LikeVo();
        likeVo.setLikeCount(likeCount);
        likeVo.setLikeStatus(likeStatus);

        //添加系统通知
        if (likeStatus == 1) {
            Event event = likeFormToEvent(likeForm);
            eventProducer.fireEvent(event);
        }

        //新增点赞后的帖子加到重新算分的缓存中
        if (likeForm.getEntityType().equals(EntityTypeEnum.DISCUSS_POST.getCode())) {
            String redisKey = RedisKeyUtil.getPostKey();
            redisTemplate.opsForSet().add(redisKey, likeForm.getEntityId());
        }

        return gson.toJson(ResponseVo.success(likeVo));
    }

    /**
     * 获取event
     *
     * @param likeForm
     * @return
     */
    private Event likeFormToEvent(LikeForm likeForm) {
        Event event = new Event();
        event.setTopic(CommunityConst.TOPIC_LIKE);
        event.setUserId(hostHolder.getUser().getId());
        event.setEntityType(likeForm.getEntityType());
        event.setEntityId(likeForm.getEntityId());
        event.setEntityUserId(likeForm.getReceiverId());
        event.setData("postId", likeForm.getPostId());
        return event;
    }
}
