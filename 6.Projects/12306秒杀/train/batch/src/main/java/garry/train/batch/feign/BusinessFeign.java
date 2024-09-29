package garry.train.batch.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Garry
 * 2024-09-29 13:55
 */
@FeignClient(name = "business", url = "http://127.0.0.1:8082/business/")
public interface BusinessFeign {

    /**
     * 效果: 请求 business 模块的 http://127.0.0.1:8082/business/hello 接口，返回接口的返回结果
     */
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    String hello();

    @RequestMapping(value = "/admin/station/query-all", method = RequestMethod.GET)
    String queryAll();
}
