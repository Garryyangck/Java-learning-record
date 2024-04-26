import garry.spring.jdbc.dao.EmployeeDao;
import garry.spring.jdbc.pojo.Employee;
import garry.spring.jdbc.service.EmployeeService;
import garry.spring.jdbc.service.EmployeeServiceDeclarativeTransaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author Garry
 * ---------2024/3/2 21:42
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class JdbcTemplateTester {
    @Resource
    private EmployeeDao employeeDao;
    @Resource
    private EmployeeService employeeService;
    @Resource
    private EmployeeServiceDeclarativeTransaction employeeServiceDeclarativeTransaction;

    @Test
    public void testFindById() throws Exception {
        try {
            Employee employee = employeeDao.findById(3308);
            System.out.println(employee);
        } catch (Exception e) {
            System.out.println("=========没有此员工=========");
            System.out.println("----->Exception:[" + e.getMessage() + "]");
            throw e;
        }
    }

    @Test
    public void testFindByDame() {
        List<Employee> employeeList = employeeDao.findByDame("研发部");
        System.out.println(employeeList);
    }

    @Test
    public void testFindMapByDame() {
        List mapByDame = employeeDao.findMapByDame("研发部");
        System.out.println(mapByDame);
    }

    @Test
    public void testInsert() {
        Employee employee = new Employee();
        employee.setEno(666);
        employee.setEname("garry");
        employee.setSalary(66666);
        employee.setDname("研发部");
        employee.setHiredate(new Date());
        employeeDao.insert(employee);
    }

    @Test
    public void testUpdate() {
        Employee employee = new Employee();
        employee.setEno(666);
        employee.setEname("garry");
        employee.setSalary(20000);
        employee.setDname("研发部");
        employee.setHiredate(new Date());
        int count = employeeDao.update(employee);
        System.out.println("影响行数:" + count);
    }

    @Test
    public void testDelete() {
        int count = employeeDao.delete(666);
        System.out.println("影响行数:" + count);
    }

    @Test
    public void testBatchImport() {
        employeeService.batchImport();
    }

    @Test
    public void testBatchImportByDeclarativeTransaction() {
        employeeServiceDeclarativeTransaction.batchImport();
    }
}
