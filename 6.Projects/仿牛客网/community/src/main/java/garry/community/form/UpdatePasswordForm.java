package garry.community.form;

import lombok.Data;

/**
 * @author Garry
 * ---------2024/3/19 19:03
 **/
@Data
public class UpdatePasswordForm {
    private String oldPassword;

    private String newPassword;
}
