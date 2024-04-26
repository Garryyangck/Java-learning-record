package garry.mall.service;

import garry.mall.pojo.User;
import garry.mall.vo.ResponseVo;

/**
 * @author Garry
 * ---------2024/3/6 19:21
 **/
@SuppressWarnings("all")
public interface IUserService {
    /**
     * 注册
     *
     * @param user
     */
    ResponseVo<User> register(User user);

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    ResponseVo<User> login(String username, String password);

}
