package garry.springbootlearn.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Garry
 * ---------2024/3/4 16:19
 **/
@Component
@ConfigurationProperties("school")//前缀为school
public class SchoolConfig {
    private Integer grade;
    private Integer classNum;

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getClassNum() {
        return classNum;
    }

    public void setClassNum(Integer classNum) {
        this.classNum = classNum;
    }
}
