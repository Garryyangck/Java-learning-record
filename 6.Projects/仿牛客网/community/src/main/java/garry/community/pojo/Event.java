package garry.community.pojo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Garry
 * ---------2024/3/26 11:38
 **/

/**
 * 消息队列的消息实体类
 */
public class Event {
    //主题，表示操作的类型，关注、点赞、评论
    private String topic;

    //操作用户
    private Integer userId;

    //操作的实体
    private Integer entityType;

    //实体id
    private Integer entityId;

    //接收者的id
    private Integer entityUserId;

    //后期扩展业务时，可能会传来的其它数据
    private Map<String, Object> data = new HashMap<>();

    public String getTopic() {
        return topic;
    }

    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public Event setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getEntityType() {
        return entityType;
    }

    public Event setEntityType(Integer entityType) {
        this.entityType = entityType;
        return this;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public Event setEntityId(Integer entityId) {
        this.entityId = entityId;
        return this;
    }

    public Integer getEntityUserId() {
        return entityUserId;
    }

    public Event setEntityUserId(Integer entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Event setData(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public Event setData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
}
