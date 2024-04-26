package com.itheima.mapper;

import com.itheima.pojo.User;

import java.util.List;

/**
 * @author Garry
 * ---------2024/2/25 14:08
 **/
public interface UserMapper {

    /**
     * 从mybatis.tb_user中搜出所有元素(集合)
     *
     * @return User
     */
    List<User> selectAll();

    /**
     * 按照id查找数据
     */
    User selectById(int id);
}
