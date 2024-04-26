package garry;

import garry.pojo.Child;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Garry
 * ---------2024/2/29 20:25
 **/
public class testAppleChild {

    @Test
    public void testIOC() {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        Child lily = context.getBean("lily", Child.class);
        Child andy = context.getBean("andy", Child.class);
        Child luna = context.getBean("luna", Child.class);
        lily.eat();
        andy.eat();
        luna.eat();
    }
}
