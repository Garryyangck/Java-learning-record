package garry.train.member.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class PassengerSaveForm {
    private Long id;

    private Long memberId;

    @NotBlank(message = "【姓名】不能为空")
    private String name;

    @NotBlank(message = "【身份证号】不能为空")
    private String idCard;

    /**
     * 乘客的类型，和 PassengerTypeEnum 相关联
     */
    @NotBlank(message = "【乘客类型】不能为空")
    private String type;

    private Date createTime;

    private Date updateTime;
}