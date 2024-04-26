package garry.community.form;

import lombok.Data;

/**
 * @author Garry
 * ---------2024/3/19 20:25
 **/
@Data
public class ForgetPasswordForm {
    private String email;

    private String verifyCode;

    private String password;
}
