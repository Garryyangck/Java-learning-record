package garry.train.member.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Garry
 * 2024-09-06 15:08
 */
@Data
public class MemberRegisterForm {
    @NotBlank(message = "【手机号】不能为空")
    private String mobile;
}
