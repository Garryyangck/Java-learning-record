package garry.spring.ioc.service;

import garry.spring.ioc.dao.UserDao;

/**
 * @author Garry
 * ---------2024/3/1 21:50
 **/
public class UserService {
    private UserDao userDao;

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        System.out.println("setUserDao...");
        this.userDao = userDao;
    }
}
