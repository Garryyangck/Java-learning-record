package com.imooc.springbootintroduction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：    读取配置类
 */
@RestController
public class ConfigController {

    @Autowired
    SchoolConfig schoolConfig;


    @GetMapping({"/gradefromconfig"})
    public String gradeclass() {
        return "年级: " + schoolConfig.grade + " 班级：" + schoolConfig.classnum;
    }
}
