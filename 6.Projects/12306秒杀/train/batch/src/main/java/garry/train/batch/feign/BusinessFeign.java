package garry.train.batch.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;

/**
 * @author Garry
 * 2024-09-29 13:55
 */
@FeignClient(name = "business", url = "http://127.0.0.1:8082/business/")
public interface BusinessFeign {

    /**
     * 效果: 请求 business 模块的 http://127.0.0.1:8082/business/hello 接口，返回接口的返回结果
     */
    @GetMapping("/hello")
    String hello();

    /**
     * 创建所有车次信息 (train-station...)
     */
    @GetMapping("/admin/daily-train/gen-daily/{date}")
    String genDaily(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date);
}
