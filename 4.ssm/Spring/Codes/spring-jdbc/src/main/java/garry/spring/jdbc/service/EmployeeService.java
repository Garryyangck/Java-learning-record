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
 * 演示编程式事务
 */
public class EmployeeService {
    private EmployeeDao employeeDao;//忘记加set方法了...
    private DataSourceTransactionManager transactionManager;

    public void batchImport() {
        //定义了事务默认的标准配置
        TransactionDefinition definition = new DefaultTransactionDefinition();
        //开始一个事务，返回事务状态，说明当前事务的执行阶段
        TransactionStatus status = transactionManager.getTransaction(definition);

        try {
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

            //提交事务
            transactionManager.commit(status);
        } catch (RuntimeException e) {
            //回滚事务
            transactionManager.rollback(status);
            try {
                throw e;
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public EmployeeDao getEmployeeDao() {
        return employeeDao;
    }

    public void setEmployeeDao(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    public DataSourceTransactionManager getTransactionManager() {
        return transactionManager;
    }

    public void setTransactionManager(DataSourceTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
}
