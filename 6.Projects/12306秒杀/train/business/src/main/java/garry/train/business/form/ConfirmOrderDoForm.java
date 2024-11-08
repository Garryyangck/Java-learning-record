package garry.train.business.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Garry
 * 2024-10-07 19:53
 */
@Data
public class ConfirmOrderDoForm {

    /**
     * id
     */
    private Long id;

    /**
     * 会员id
     */
    private Long memberId;

    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @NotNull(message = "【日期】不能为空")
    private Date date;

    /**
     * 车次编号
     */
    @NotBlank(message = "【车次编号】不能为空")
    private String trainCode;

    /**
     * 出发站
     */
    @NotBlank(message = "【出发站】不能为空")
    private String start;

    /**
     * 到达站
     */
    @NotBlank(message = "【到达站】不能为空")
    private String end;

    /**
     * 余票ID
     */
    @NotNull(message = "【余票ID】不能为空")
    private Long dailyTrainTicketId;

    /**
     * 车票
     */
    @NotNull(message = "【车票】不能为空")
    private List<ConfirmOrderTicketForm> tickets;

    /**
     * 图片验证码 token
     */
    @NotBlank(message = "【图片验证码 token】不能为空")
    private String imageCodeToken;

    /**
     * 图片验证码
     */
    @NotBlank(message = "【图片验证码】不能为空")
    private String imageCode;

    /**
     * MDC LOG_ID，以便 doConfirm 异步执行时，能打印跟踪号
     */
    private String LOG_ID;

}
