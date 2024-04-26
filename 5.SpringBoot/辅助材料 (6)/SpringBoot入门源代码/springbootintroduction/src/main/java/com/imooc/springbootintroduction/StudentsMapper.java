package com.imooc.springbootintroduction;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 描述：     Mapper类
 */
@Mapper
@Repository
public interface StudentsMapper {

    @Select("SELECT * FROM students WHERE id = #{id}")
    Student findById(long id);
}
