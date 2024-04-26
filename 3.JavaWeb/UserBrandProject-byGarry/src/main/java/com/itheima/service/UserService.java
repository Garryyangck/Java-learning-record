package com.itheima.service;

import com.itheima.mapper.UserMapper;
import com.itheima.pojo.User;
import com.itheima.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.swing.plaf.multi.MultiSeparatorUI;

/**
 * @author Garry
 * ---------2024/2/28 19:42
 **/
@SuppressWarnings({"all"})
public class UserService {
    //只获取一次工厂对象
    public static final SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    /**
     * 登录方法
     *
     * @param username
     * @param password
     * @return
     */
    public User login(String username, String password) {
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);//反射机制

        User user = mapper.select(username, password);

        sqlSession.close();
        return user;
    }

    /**
     * 注册方法
     *
     * @param username
     * @return
     */
    public boolean register(User user) {
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);//反射机制

        User selectByUsername = mapper.selectByUsername(user.getUsername());

        if (selectByUsername == null) {
            mapper.add(user);
            sqlSession.commit();
        }
        sqlSession.close();
        return selectByUsername == null;
    }
}
