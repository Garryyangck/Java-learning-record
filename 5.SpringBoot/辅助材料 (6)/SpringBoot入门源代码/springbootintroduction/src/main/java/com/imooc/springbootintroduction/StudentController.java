package com.imooc.springbootintroduction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：StudentController
 */
@RestController
public class StudentController {

    @Autowired
    StudentService studentService;

    @GetMapping({"/student"})
    public String requestpara(@RequestParam Integer id) {
        return studentService.getStudent(id).toString();
    }

}
