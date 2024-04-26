package garry.community.form;

import lombok.Data;

/**
 * @author Garry
 * ---------2024/3/18 14:36
 **/
@Data
public class UserLoginForm {
    private String username;

    private String password;

    private String verifyCode;

    private Boolean remember = false;
}
