package garry.train.member.controller;

import garry.train.common.util.HostHolder;
import garry.train.common.vo.PageVo;
import garry.train.common.vo.ResponseVo;
import garry.train.member.form.TicketQueryForm;
import garry.train.member.service.TicketService;
import garry.train.member.vo.TicketQueryVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Garry
 * 2024-10-14 15:19
 */
@RestController
@RequestMapping(value = "/ticket")
public class TicketController {
    @Resource
    private TicketService ticketService;

    @Resource
    private HostHolder hostHolder;

    @RequestMapping(value = "/query-list", method = RequestMethod.GET)
    public ResponseVo<PageVo<TicketQueryVo>> queryList(@Valid TicketQueryForm form) {
        form.setMemberId(hostHolder.getMemberId()); // service 层是管理员和用户通用的接口，只有用户才需要取 memberId，因此取 memberId 的操作在 Controller 层实现
        PageVo<TicketQueryVo> vo = ticketService.queryList(form);
        return ResponseVo.success(vo);
    }
}
