package garry.spring.jdbc.service;

import garry.spring.jdbc.dao.EmployeeDao;
import garry.spring.jdbc.pojo.Employee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author Garry
 * ---------2024/3/3 9:44
 **/

/**
 * 演示注解配置声明式事务
 */
@SuppressWarnings({"all"})
@Service
/**
 * 声明式事务核心注解
 * 放在类上,将声明式事务配置应用于当前类所有方法,默认事务传播为 REQUIRED
 */
@Transactional(propagation = Propagation.REQUIRED)
public class EmployeeService {
    @Resource
    private EmployeeDao employeeDao;

    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public Employee findById(int eno) {
        Employee employee = employeeDao.findById(eno);
        return employee;
    }

    public void batchImport() {
        for (int i = 0; i < 10; i++) {
            if (i == 3) throw new RuntimeException("意料之外的异常...");
            Employee employee = new Employee();
            employee.setEno(8000 + i);
            employee.setEname("员工" + i);
            employee.setSalary(5000);
            employee.setDname("市场部");
            employee.setHiredate(new Date());
            employeeDao.insert(employee);
        }
    }

    public EmployeeDao getEmployeeDao() {
        return employeeDao;
    }

    public void setEmployeeDao(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }
}
