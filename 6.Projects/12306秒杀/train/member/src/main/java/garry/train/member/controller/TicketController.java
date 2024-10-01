package garry.train.member.controller;

import garry.train.common.util.HostHolder;
import garry.train.common.vo.PageVo;
import garry.train.common.vo.ResponseVo;
import garry.train.member.form.TicketQueryForm;
import garry.train.member.form.TicketSaveForm;
import garry.train.member.service.TicketService;
import garry.train.member.vo.TicketQueryVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author Garry
 * 2024-10-01 17:21
 */
@RestController
@RequestMapping(value = "/ticket")
public class TicketController {
    @Resource
    private TicketService ticketService;

    @Resource
    private HostHolder hostHolder;

    /**
     * 接收新增和修改车票的请求，如果 form.id = null，则为新增；反之为修改
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseVo save(@Valid @RequestBody TicketSaveForm form) {
        form.setMemberId(hostHolder.getMemberId());
        ticketService.save(form);
        return ResponseVo.success();
    }

    @RequestMapping(value = "/query-list", method = RequestMethod.GET)
    public ResponseVo<PageVo<TicketQueryVo>> queryList(@Valid TicketQueryForm form) {
        form.setMemberId(hostHolder.getMemberId()); // service 层是管理员和用户通用的接口，只有用户才需要取 memberId，因此取 memberId 的操作在 Controller 层实现
        PageVo<TicketQueryVo> vo = ticketService.queryList(form);
        return ResponseVo.success(vo);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseVo delete(@PathVariable Long id) {
        ticketService.delete(id);
        return ResponseVo.success();
    }
}
