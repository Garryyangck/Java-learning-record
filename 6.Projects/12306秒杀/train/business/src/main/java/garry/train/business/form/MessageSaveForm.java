package garry.train.business.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

/**
 * @author Garry
 * 2024-10-11 14:24
 */
@Data
public class MessageSaveForm {

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
    @NotNull(message = "【接收者id】不能为空")
    private Long toId;

    /**
     * 消息类型|枚举[MessageTypeEnum]
     */
    @NotBlank(message = "【消息类型】不能为空")
    private String type;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息状态|枚举[MessageStatusEnum]
     */
    @NotBlank(message = "【消息状态】不能为空")
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
