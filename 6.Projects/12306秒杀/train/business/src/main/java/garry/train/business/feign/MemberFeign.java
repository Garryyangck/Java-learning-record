package garry.train.business.feign;

import garry.train.business.form.TicketSaveForm;
import garry.train.common.form.ApiDetailQueryForm;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Garry
 * 2024-10-14 16:16
 */
@FeignClient(name = "member", url = "http://127.0.0.1:8081/member/")
public interface MemberFeign {

    /**
     * 存储 Ticket
     */
    @PostMapping("/feign/ticket/save")
    String save(@Valid @RequestBody TicketSaveForm form);

    /**
     * 获取 member 模块的 ApiDetail
     */
    @PostMapping("/admin/api-detail/query-list")
    String queryList(@Valid @RequestBody ApiDetailQueryForm form);
}
