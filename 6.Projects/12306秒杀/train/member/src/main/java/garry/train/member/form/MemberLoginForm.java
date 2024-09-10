package garry.train.member.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * @author Garry
 * 2024-09-10 15:07
 */
@Data
public class MemberLoginForm {
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^$|^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$",
            message = "手机号码格式不正确")
    private String mobile;

    @NotBlank(message = "验证码不能为空")
    private String code;
}
