package garry.mall.form;

/**
 * @author Garry
 * ---------2024/3/6 21:42
 **/

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户模块传给Controller的json格式的对象
 */
@Data
public class UserRegisterForm {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String email;
}
