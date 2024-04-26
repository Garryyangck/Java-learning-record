package garry.spring.ioc;

import com.sun.javaws.IconUtil;
import garry.spring.ioc.pojo.Company;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Properties;

/**
 * @author Garry
 * ---------2024/3/1 11:28
 **/
public class SpringApplication {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        Company company = context.getBean("company", Company.class);
        System.out.println(company);
        Properties info = company.getInfo();
        String website = info.getProperty("website");
        System.out.println(website);
        System.out.println("======================");
        String[] beanNames = context.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }


        context.registerShutdownHook();
    }
}
