package garry.train.business.vo;

import lombok.Data;

/**
 * @author Garry
 * 2024-10-11 18:12
 */
@Data
public class MessageSendVo {

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
     * 接收方未读的消息总数
     */
    private Integer unreadNum;
}
