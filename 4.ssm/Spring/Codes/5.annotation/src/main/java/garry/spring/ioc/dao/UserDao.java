package garry.spring.ioc.dao;

import org.springframework.stereotype.Repository;

/**
 * @author Garry
 * ---------2024/3/1 19:00
 **/
@Repository
public class UserDao implements IUserDao{
    public UserDao() {
        System.out.println("创建UserDao..." + this);
    }
}
