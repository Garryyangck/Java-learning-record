package garry.springbootlearn.controller;

/**
 * @author Garry
 * ---------2024/3/4 16:23
 **/

import garry.springbootlearn.config.SchoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 读取配置类
 */
@RestController
@RequestMapping("/config")
public class ConfigController {
    @Resource
    private SchoolConfig schoolConfig;

    @GetMapping({"/gradeClass"})
    public String gradeClass() {
        return schoolConfig.getGrade() + ":" + schoolConfig.getClassNum();
    }
}
