package garry.spring.ioc.service;

import garry.spring.ioc.dao.IUserDao;
import garry.spring.ioc.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author Garry
 * ---------2024/3/1 19:09
 **/
@Service
@Scope("prototype")
public class UserService {
    @Value("${metaData}")
    private String metaData;

    @Resource(name = "userDao")
    private IUserDao iUserDao;

    @PostConstruct
    public void init() {
        System.out.println("UserService类初始化...");
    }

    public UserService() {
        System.out.println("创建UserService..." + this);
    }

    public String getMetaData() {
        return metaData;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }

    public IUserDao getiUserDao() {
        return iUserDao;
    }

    public void setiUserDao(IUserDao iUserDao) {
        this.iUserDao = iUserDao;
    }
}
