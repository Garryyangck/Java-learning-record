package garry.spring.jdbc.dao;

import garry.spring.jdbc.pojo.Employee;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Garry
 * ---------2024/3/2 21:12
 **/
@SuppressWarnings({"all"})
@Repository
public class EmployeeDao {
    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据id查找，返回唯一对象，没有会报错
     *
     * @param eno
     * @return
     */
    public Employee findById(Integer eno) {
        String sql = "select * from employee where eno = ?";
        //queryForObject查询单条数据
        //(sql语句，参数数组，负责字段属性转换的BeanPropertyRowMapper对象)
        Employee employee = jdbcTemplate.queryForObject(sql, new Object[]{eno},
                new BeanPropertyRowMapper<Employee>(Employee.class));
        return employee;
    }

    /**
     * 根据dname查找，返回对象数组
     *
     * @param dname
     * @return
     */
    public List<Employee> findByDame(String dname) {
        String sql = "select * from employee where dname = ?";
        //query查询复合数据
        List<Employee> list = jdbcTemplate.query(sql, new Object[]{dname},
                new BeanPropertyRowMapper<Employee>(Employee.class));
        return list;
    }

    /**
     * 返回Map<String, Object>型数组，前面的String优先使用别名
     *
     * @param dname
     * @return
     */
    public List findMapByDame(String dname) {
        String sql = "select eno as 员工编号, ename as 员工姓名,  salary as 员工工资 from employee where dname = ?";
        //区别，queryForList没有第三个参数，直接以别名->对象形式储存到Map中
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, new Object[]{dname});
        return maps;
    }

    /**
     * 插入
     *
     * @param employee
     */
    public void insert(Employee employee) {
        String sql = "insert into employee(eno,ename,salary,dname,hiredate) values(?,?,?,?,?)";

        jdbcTemplate.update(sql, new Object[]{
                employee.getEno(),
                employee.getEname(),
                employee.getSalary(),
                employee.getDname(),
                employee.getHiredate()
        });
        //不用手动提交事务
    }

    /**
     * 更新
     *
     * @param employee
     * @return
     */
    public int update(Employee employee) {
        String sql = "update employee set ename = ?,salary = ?,dname = ?,hiredate = ? where eno = ?";

        int count = jdbcTemplate.update(sql, new Object[]{
                employee.getEname(),
                employee.getSalary(),
                employee.getDname(),
                employee.getHiredate(),
                employee.getEno(),
        });
        //不用手动提交事务
        return count;
    }

    /**
     * 删除
     *
     * @param eno
     * @return
     */
    public int delete(int eno) {
        String sql = "delete from employee where eno = ?";
        int count = jdbcTemplate.update(sql, new Object[]{eno});
        return count;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
