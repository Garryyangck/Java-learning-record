package garry.spring.aop.service;

/**
 * @author Garry
 * ---------2024/3/2 16:57
 **/

/**
 * 委托类
 */
public class UserServiceImpl implements UserService {
    public void createUser() {
        System.out.println("创建用户...");
    }
}
