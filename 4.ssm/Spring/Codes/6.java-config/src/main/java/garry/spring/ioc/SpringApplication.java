package garry.spring.ioc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Garry
 * ---------2024/3/1 21:54
 **/
public class SpringApplication {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        String[] ids = context.getBeanDefinitionNames();
        for (String id : ids) {
            System.out.println(id + ":" + context.getBean(id));
        }
    }
}
