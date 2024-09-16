package garry.train.member.controller;

import garry.train.common.util.HostHolder;
import garry.train.common.vo.PageVo;
import garry.train.common.vo.ResponseVo;
import garry.train.member.form.PassengerQueryForm;
import garry.train.member.form.PassengerSaveForm;
import garry.train.member.service.PassengerService;
import garry.train.member.vo.PassengerQueryVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/passenger")
public class PassengerController {
    @Resource
    private PassengerService passengerService;

    @Resource
    private HostHolder hostHolder;

    /**
     * 接收新增和修改乘车人的请求，如果 form.id = null，则为新增；反之位修改
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseVo save(@Valid @RequestBody PassengerSaveForm form) {
        form.setMemberId(hostHolder.getMemberId());
        passengerService.save(form);
        return ResponseVo.success();
    }

    @RequestMapping(value = "/query-list", method = RequestMethod.GET)
    public ResponseVo<PageVo<PassengerQueryVo>> queryList(@Valid PassengerQueryForm form) {
        form.setMemberId(hostHolder.getMemberId()); // service 层是管理员和用户通用的接口，只有用户才需要取 memberId，因此取 memberId 的操作在 Controller 层实现。
        PageVo<PassengerQueryVo> vo = passengerService.queryList(form);
        return ResponseVo.success(vo);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseVo delete(@PathVariable Long id) {
        passengerService.delete(id);
        return ResponseVo.success();
    }
}
