package garry.mall.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Garry
 * ---------2024/3/7 20:36
 **/
@Data
public class UserLoginForm {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
