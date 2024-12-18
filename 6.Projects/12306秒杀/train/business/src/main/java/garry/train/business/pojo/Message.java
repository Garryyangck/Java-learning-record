package garry.train.business.pojo;

import lombok.Data;
import java.util.Date;

/**
 * @author Garry
 * 2024-10-11 14:24
 */
@Data
public class Message {

    /**
     * id
     */
    private Long id;

    /**
     * 发出者id，系统消息则为0
     */
    private Long fromId;

    /**
     * 接收者id
     */
    private Long toId;

    /**
     * 消息类型|枚举[MessageTypeEnum]
     */
    private String type;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息状态|枚举[MessageStatusEnum]
     */
    private String status;

    /**
     * 新增时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}