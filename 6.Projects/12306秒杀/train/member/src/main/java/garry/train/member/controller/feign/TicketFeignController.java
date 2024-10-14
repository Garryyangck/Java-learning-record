package garry.train.member.controller.feign;

import garry.train.common.vo.ResponseVo;
import garry.train.member.form.TicketSaveForm;
import garry.train.member.service.TicketService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Garry
 * 2024-10-14 15:19
 */
@RestController
@RequestMapping(value = "/feign/ticket")
public class TicketFeignController {
    @Resource
    private TicketService ticketService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseVo save(@Valid @RequestBody TicketSaveForm form) {
        ticketService.save(form);
        return ResponseVo.success();
    }
}
