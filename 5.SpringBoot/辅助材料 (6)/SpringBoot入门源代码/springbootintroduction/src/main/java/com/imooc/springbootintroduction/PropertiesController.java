package com.imooc.springbootintroduction;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：     演示读取配置文件的值
 */
@RestController
public class PropertiesController {


    @Value("${school.grade}")
    Integer grade;

    @Value("${school.classnum}")
    Integer classnum;


    @GetMapping({"/gradeclass"})
    public String gradeclass() {
        return "年级: " + grade + " 班级：" + classnum;
    }


}
