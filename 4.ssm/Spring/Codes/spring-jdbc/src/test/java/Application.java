import garry.spring.jdbc.dao.EmployeeDao;
import garry.spring.jdbc.pojo.Employee;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Garry
 * ---------2024/3/2 21:26
 **/
public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        EmployeeDao employeeDao = (EmployeeDao) context.getBean("employeeDao");
        Employee employee = employeeDao.findById(3308);
        System.out.println(employee);
    }
}
