package garry.springbootlearn.controller;

import garry.springbootlearn.pojo.Student;
import garry.springbootlearn.service.StudentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Garry
 * ---------2024/3/4 16:54
 **/
@RestController
@RequestMapping("/student")
public class StudentController {
    @Resource
    private StudentService studentService;

    /**
     * 通过学号查找学生
     *
     * @param num
     * @return
     */
    @GetMapping({"/find"})
    public String find(@RequestParam(required = false, defaultValue = "0") Integer num) {
        Student student = studentService.findStudent(num);
        return student.toString();
    }
}
