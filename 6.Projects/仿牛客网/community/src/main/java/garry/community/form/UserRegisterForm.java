package garry.community.form;

import lombok.Data;

/**
 * @author Garry
 * ---------2024/3/17 16:00
 **/

/**
 * 用户注册请求形式
 * 这里不用@NotBlank:因为LoginController.register中没有使用@RequestBody，即不是以Json形式接收参数
 */
@Data
public class UserRegisterForm {
    private String username;

    private String password;

    private String email;
}
