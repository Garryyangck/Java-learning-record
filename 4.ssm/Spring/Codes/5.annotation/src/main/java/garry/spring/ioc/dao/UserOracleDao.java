package garry.spring.ioc.dao;

import org.springframework.stereotype.Repository;

/**
 * @author Garry
 * ---------2024/3/1 20:28
 **/
@Repository
public class UserOracleDao implements IUserDao {
    public UserOracleDao() {
        System.out.println("创建UserOracleDao..." + this);
    }
}
