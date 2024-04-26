package garry.community.service;

import garry.community.enums.ActivationEnum;
import garry.community.pojo.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;

/**
 * @author Garry
 * ---------2024/3/15 20:33
 **/
public interface UserService {

    /**
     * 使用redis缓存用户数据，提升查询效率
     *
     * @param userId
     * @return
     */
    User findByUserId(Integer userId);

    User findByUsername(String username);

    User findByEmail(String email);

    int updateHeader(int userId, String headerUrl);

    int updatePassword(int userId, String password);

    /**
     * 接收前端的注册表单，执行可行性判断的业务逻辑
     *
     * @param user
     * @return 给前端的返回信息(比如usernameMsg, emailMsg等)
     */
    Map<String, Object> register(User user);

    /**
     * 根据userId和activationCode对用户账号进行激活
     *
     * @param userId
     * @param activationCode
     * @return 激活状态
     */
    ActivationEnum activation(Integer userId, String activationCode);

    /**
     * 根据用户输入的用户名和密码判断登录是否成功(验证码的检验和记住密码在Controller里)
     * 并创建登录凭证
     *
     * @param username
     * @param password
     * @param expiredSeconds
     * @return 给前端的返回信息(比如usernameMsg, emailMsg等)
     */
    Map<String, Object> login(String username, String password, Long expiredSeconds);

    /**
     * 将redis中的登录凭证的状态改为无效
     *
     * @param ticket
     */
    void logout(String ticket);

    /**
     * 向用户的邮箱中发送找回密码验证码，返回验证码
     *
     * @param email
     * @return
     */
    String sendVerifyCode(String email);

    /**
     * 获取用户的权限
     *
     * @param userId
     * @return
     */
    Collection<? extends GrantedAuthority> getAuthorities(Integer userId);
}
