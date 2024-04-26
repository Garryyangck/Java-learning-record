package garry.spring.aop.service;

/**
 * @author Garry
 * ---------2024/3/2 16:58
 **/

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 代理类
 */
public class UserServiceProxyAfter implements UserService {
    //持有委托类的对象
    private UserService userService;

    public UserServiceProxyAfter(UserService userService) {
        this.userService = userService;
    }

    public void createUser() {
        //持有委托类对象，可以调用委托类的方法
        userService.createUser();
        System.out.println("=============后置扩展=============");
    }
}
