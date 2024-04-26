package garry.spring.aop;

import garry.spring.aop.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author Garry
 * ---------2024/3/2 13:19
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class SpringAOPApplicationTest {
    @Resource
    private UserService userService;

    @Test
    public void test() throws Exception{
        userService.createUser();
        userService.generateRandomPassword("MD5", 8);
    }
}
