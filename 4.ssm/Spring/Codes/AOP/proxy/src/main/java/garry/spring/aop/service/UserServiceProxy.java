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
public class UserServiceProxy implements UserService {
    //持有委托类的对象
    private UserService userService;

    public UserServiceProxy(UserService userService) {
        this.userService = userService;
    }

    public void createUser() {
        //在委托类的基础上，对代理类进行扩展
        System.out.println("=========" + new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss.SSS]").format(new Date()) + "=========");

        //持有委托类对象，可以调用委托类的方法
        userService.createUser();
    }
}
