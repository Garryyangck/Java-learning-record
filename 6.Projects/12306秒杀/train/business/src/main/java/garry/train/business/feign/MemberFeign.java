package garry.train.business.feign;

import garry.train.business.form.TicketSaveForm;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Garry
 * 2024-10-14 16:16
 */
@FeignClient("member")
//@FeignClient(name = "member", url = "http://127.0.0.1:8081/member/")
public interface MemberFeign {

    @GetMapping("/member/hello")
    String hello();

    /**
     * 存储 Ticket
     */
    @PostMapping("/member/feign/ticket/save")
    String save(@Valid @RequestBody TicketSaveForm form);

    /**
     * 获取 member 模块的 ApiDetail
     * 报错：Request method 'POST' is not supported 的解决方法
     * 把 form 中的参数单独提出来，并带上 @RequestParam 注解
     */
    @GetMapping("/member/admin/api-detail/query-list")
    String queryList(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize);
}
