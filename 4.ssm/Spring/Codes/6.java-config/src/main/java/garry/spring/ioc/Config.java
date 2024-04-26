package garry.spring.ioc;

import garry.spring.ioc.controller.UserController;
import garry.spring.ioc.dao.UserDao;
import garry.spring.ioc.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Garry
 * ---------2024/3/1 21:51
 **/
@Configuration//当前类是一个配置类,用于替代applicationContext.xml
@ComponentScan(basePackages = "garry")
public class Config {
    @Bean//Java config利用方法创建对象,将方法返回对象放入容器,beanId=方法名
    public UserDao userDao() {
        UserDao userDao = new UserDao();
        return userDao;
    }

    @Bean
    public UserService userService(UserDao userDao) {//注入方式，参数中加上userDao
        //程序会检测到需要注入的就是上面创建的userDao
        UserService userService = new UserService();
        userService.setUserDao(userDao);
        return userService;
    }

    @Bean
    public UserController userController(UserService userService) {
        UserController userController = new UserController();
        userController.setUserService(userService);
        return userController;
    }
}
