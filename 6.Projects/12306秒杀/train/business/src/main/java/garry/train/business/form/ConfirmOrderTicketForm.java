package garry.train.business.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Garry
 * 2024-10-07 19:53
 */

/**
 * 前端调用接口时，需要传入的 tickets 的对象的映射类
 */
@Data
public class ConfirmOrderTicketForm {

    /**
     * 乘车人ID
     */
    @NotNull(message = "【乘车人ID】不能为空")
    private Long passengerId;

    /**
     * 乘车人类型
     */
    @NotBlank(message = "【乘车人类型】不能为空")
    private String passengerType;

    /**
     * 乘车人姓名
     */
    @NotBlank(message = "【乘车人姓名】不能为空")
    private String passengerName;

    /**
     * 乘车人身份证
     */
    @NotBlank(message = "【乘车人身份证】不能为空")
    private String passengerIdCard;

    /**
     * 座位类型
     */
    @NotBlank(message = "【座位类型】不能为空")
    private String seatTypeCode;

    /**
     * 座位号
     */
    @NotBlank(message = "【座位号】不能为空")
    private String seat;

}
