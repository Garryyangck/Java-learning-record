package garry.spring.aop.dao;

import org.springframework.stereotype.Repository;

/**
 * 用户表Dao
 */
@Repository
public class UserDao {
    public void insert(){
        System.out.println("新增用户数据");
    }
}
