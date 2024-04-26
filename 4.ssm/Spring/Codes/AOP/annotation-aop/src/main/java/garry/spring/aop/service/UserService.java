package garry.spring.aop.service;


import garry.spring.aop.dao.UserDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户服务
 */
@Service
public class UserService {
    @Resource//运行时，ioc将private改成public，直接对其赋值
    private UserDao userDao;

    public void createUser() throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("执行用户加入业务逻辑");
        userDao.insert();
    }

    public String generateRandomPassword(String type, Integer length) {
        System.out.println("按" + type + "方式生成" + length + "位随机密码");
        return "Zxcquei1";
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
