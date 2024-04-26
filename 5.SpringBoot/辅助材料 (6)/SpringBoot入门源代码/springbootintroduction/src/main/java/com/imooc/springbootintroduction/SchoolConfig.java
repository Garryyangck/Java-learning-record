package com.imooc.springbootintroduction;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 描述：     配置类
 */
@Component
@ConfigurationProperties(prefix = "school")
public class SchoolConfig {

    Integer grade;

    Integer classnum;

    public Integer getGrade() {
        return grade;
    }

    public Integer getClassnum() {
        return classnum;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public void setClassnum(Integer classnum) {
        this.classnum = classnum;
    }
}
