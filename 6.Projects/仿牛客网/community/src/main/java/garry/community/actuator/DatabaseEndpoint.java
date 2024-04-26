package garry.community.actuator;

import com.google.gson.Gson;
import garry.community.enums.ResponseEnum;
import garry.community.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Garry
 * ---------2024/3/31 19:41
 **/

/**
 * 自定义断点，检查是否能连接数据库
 */
@Slf4j
@Configuration
@Endpoint(id = "database")//注意此处不能是"/database，否则报错：Value must only contain valid chars
public class DatabaseEndpoint {
    @Resource
    private DataSource dataSource;

    private static final Gson gson = new Gson();

    @ReadOperation/*这个接口只能通过GET请求访问，当然还有WriteOption，只能被PUT访问*/
    public String checkConnection() {
        try (Connection connection = dataSource.getConnection()) {
            return gson.toJson(ResponseVo.success());
        } catch (SQLException e) {
            log.error("【获取数据库连接失败】");
            return gson.toJson(ResponseVo.error(ResponseEnum.CONNECTION_FAILURE));
        }
    }
}
