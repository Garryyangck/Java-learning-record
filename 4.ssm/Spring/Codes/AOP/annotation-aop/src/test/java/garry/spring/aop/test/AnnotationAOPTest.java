package garry.spring.aop.test;

import garry.spring.aop.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author Garry
 * ---------2024/3/2 16:28
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class AnnotationAOPTest {
    @Resource(name = "userService")
    private UserService userService;

    @Test
    public void test() throws InterruptedException {
        userService.createUser();
        userService.generateRandomPassword("MD5", 8);
    }
}
