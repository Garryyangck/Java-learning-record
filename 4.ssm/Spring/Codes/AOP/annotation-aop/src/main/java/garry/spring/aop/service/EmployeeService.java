package garry.spring.aop.service;


import garry.spring.aop.dao.EmployeeDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 员工服务
 */
@Service
public class EmployeeService {

    private EmployeeDao employeeDao;

    public void entry() {
        System.out.println("执行员工入职业务逻辑");
        employeeDao.insert();
    }

    public EmployeeDao getEmployeeDao() {
        return employeeDao;
    }

    @Resource//采用set方法完成注入
    public void setEmployeeDao(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }
}
