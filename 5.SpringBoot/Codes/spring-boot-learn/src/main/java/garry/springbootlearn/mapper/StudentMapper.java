package garry.springbootlearn.mapper;

import garry.springbootlearn.pojo.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author Garry
 * ---------2024/3/4 17:02
 **/
@Mapper
@Repository
public interface StudentMapper {
    @Select("select * from student where id=#{id}")
    Student selectById(@Param("id") Integer id);
}
