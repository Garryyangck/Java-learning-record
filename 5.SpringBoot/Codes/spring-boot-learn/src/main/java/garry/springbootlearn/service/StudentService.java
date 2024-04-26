package garry.springbootlearn.service;

import garry.springbootlearn.mapper.StudentMapper;
import garry.springbootlearn.pojo.Student;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Garry
 * ---------2024/3/4 16:55
 **/
@Service
public class StudentService {
    @Resource
    private StudentMapper studentMapper;

    public Student findStudent(Integer id) {
        Student student = studentMapper.selectById(id);
        return student;
    }
}
