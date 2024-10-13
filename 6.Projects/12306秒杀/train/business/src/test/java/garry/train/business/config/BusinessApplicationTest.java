package garry.train.business.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Garry
 * 2024-10-13 15:21
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = BusinessApplication.class)
public class BusinessApplicationTest {

    @Test
    public void contextLoads() {
    }
}