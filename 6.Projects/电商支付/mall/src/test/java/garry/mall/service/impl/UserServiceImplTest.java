package garry.mall.service.impl;

import garry.mall.MallApplicationTest;
import garry.mall.dao.UserMapper;
import garry.mall.enums.ResponseEnum;
import garry.mall.enums.RoleEnum;
import garry.mall.pojo.User;
import garry.mall.service.IUserService;
import garry.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author Garry
 * ---------2024/3/6 19:47
 **/
@Slf4j
@Transactional//执行rollback操作，可以让单源测试不影响数据库
public class UserServiceImplTest extends MallApplicationTest {
    public static final String USERNAME = "jack";

    public static final String PASSWORD = "123456";

    @Resource
    private IUserService userService;

    @Resource
    private UserMapper userMapper;

    @Before
    public void register() {
        User user = new User(USERNAME, PASSWORD, "jack@qq.com", RoleEnum.CUSTOMER.getCode());
        ResponseVo<User> responseVo = userService.register(user);
        Assert.assertEquals(ResponseEnum.SUCCESS.getStatus(), responseVo.getStatus());
        log.info("register success!");
    }

    @Test
    public void login() {
        ResponseVo<User> responseVo = userService.login(USERNAME, PASSWORD);
        //断言
        Assert.assertEquals(ResponseEnum.SUCCESS.getStatus(), responseVo.getStatus());
        log.info("login success! The newly registered username is {}", USERNAME);
    }

    @After
    public void rollback() {
        userMapper.deleteByPrimaryKey(userMapper.selectByUsername(USERNAME).getId());
    }
}