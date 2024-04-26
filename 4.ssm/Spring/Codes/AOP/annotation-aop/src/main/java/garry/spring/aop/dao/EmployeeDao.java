package garry.spring.aop.dao;

import org.springframework.stereotype.Repository;

/**
 * 员工表Dao
 */
@Repository
public class EmployeeDao {
    public void insert(){
        System.out.println("新增员工数据");
    }
}
