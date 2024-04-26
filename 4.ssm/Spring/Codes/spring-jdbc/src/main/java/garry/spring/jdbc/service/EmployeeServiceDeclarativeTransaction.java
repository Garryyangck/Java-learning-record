package garry.spring.jdbc.service;

import garry.spring.jdbc.dao.EmployeeDao;
import garry.spring.jdbc.pojo.Employee;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Date;

/**
 * @author Garry
 * ---------2024/3/3 9:44
 **/

/**
 * 演示声明式事务
 */
@SuppressWarnings({"all"})
public class EmployeeServiceDeclarativeTransaction {
    private EmployeeDao employeeDao;

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
