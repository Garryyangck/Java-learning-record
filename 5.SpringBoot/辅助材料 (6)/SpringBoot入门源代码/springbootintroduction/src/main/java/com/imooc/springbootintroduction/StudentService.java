package com.imooc.springbootintroduction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * 描述：     Student的Service
 */
@Service
public class StudentService {

    @Autowired
    StudentsMapper studentsMapper;


    public Student getStudent(Integer id) {
        return studentsMapper.findById(1);
    }
}
