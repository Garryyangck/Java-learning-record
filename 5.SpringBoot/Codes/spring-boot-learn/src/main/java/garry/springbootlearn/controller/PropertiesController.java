package garry.springbootlearn.controller;

/**
 * @author Garry
 * ---------2024/3/4 16:11
 **/

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 演示读取配置的Controller
 */
@RestController
public class PropertiesController {
    @Value("${school.grade}")//绑定
    Integer grade;
    @Value("${school.classNum}")
    Integer classNum;

    @GetMapping({"/gradeClass"})
    public String gradeClass() {
        return grade + ":" + classNum;
    }
}
