package com.imooc.springbootintroduction;

/**
 * 描述：     Student实体类
 */
public class Student {

    Integer id;
    String name;

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
