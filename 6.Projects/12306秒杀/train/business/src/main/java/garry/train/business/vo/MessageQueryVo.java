package garry.train.business.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

/**
 * @author Garry
 * 2024-10-11 14:24
 */
@Data
public class MessageQueryVo {

    /**
     * id
     */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    /**
     * 发出者id，系统消息则为0
     */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long fromId;

    /**
     * 接收者id
     */
    @JsonSerialize(using= ToStringSerializer.class)
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

}
