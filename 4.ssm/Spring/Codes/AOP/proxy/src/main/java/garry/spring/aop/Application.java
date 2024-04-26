package garry.spring.aop;

import garry.spring.aop.service.UserService;
import garry.spring.aop.service.UserServiceImpl;
import garry.spring.aop.service.UserServiceProxy;
import garry.spring.aop.service.UserServiceProxyAfter;

/**
 * @author Garry
 * ---------2024/3/2 17:37
 **/
public class Application {
    public static void main(String[] args) {
        UserService userServiceProxy = new UserServiceProxyAfter(new UserServiceProxy(new UserServiceImpl()));
        userServiceProxy.createUser();
    }
}
