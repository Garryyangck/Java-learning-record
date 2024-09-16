package garry.train.member.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

@Data
public class PassengerSaveForm {
    private Long id; // 新增不需要赋值，修改需要

    private Long memberId; // 通过 Controller 赋值

    @NotBlank(message = "【姓名】不能为空")
    private String name;

    @NotBlank(message = "【身份证号】不能为空")
    private String idCard;

    /**
     * 乘客的类型，和 PassengerTypeEnum 相关联
     */
    @NotBlank(message = "【乘客类型】不能为空")
    private String type;

    private Date createTime; // 新增需要赋值，修改不需要

    private Date updateTime; // 新增和修改均要赋值
}