package garry.spring.ioc.service;

import garry.spring.ioc.dao.UserDao;

/**
 * @author Garry
 * ---------2024/3/2 10:10
 **/
public class UserService {
    private UserDao userDao;

    public void createUser() {
        System.out.println("调用UserDao的insert方法...");
        userDao.insert();
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
