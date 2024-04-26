package garry.spring.ioc.controller;

import garry.spring.ioc.service.UserService;

/**
 * @author Garry
 * ---------2024/3/1 21:50
 **/
public class UserController {
    private UserService userService;

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        System.out.println("setUserService...");
        this.userService = userService;
    }
}
