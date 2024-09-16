package garry.train.member.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Garry
 * 2024-09-16 17:59
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MemberApplication.class)
public class MemberApplicationTest {

    @Test
    public void contextLoads() {
    }
}