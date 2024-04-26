import garry.spring.ioc.dao.UserDao;
import garry.spring.ioc.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author Garry
 * ---------2024/3/2 10:14
 **/
//将Junit4的执行权交由Spring Test,在测试用例执行前自动初始化IoC容器
@RunWith(SpringJUnit4ClassRunner.class)
//ioc初始化过程中，通知ioc要加载哪个配置文件
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class SpringTest {
    @Resource
    private UserService userService;

    @Test
    public void testUserService() {
        userService.createUser();
    }
}
