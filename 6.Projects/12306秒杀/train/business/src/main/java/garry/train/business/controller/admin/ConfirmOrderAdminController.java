package garry.train.business.controller.admin;

import garry.train.business.form.ConfirmOrderQueryForm;
import garry.train.business.form.ConfirmOrderDoForm;
import garry.train.business.service.ConfirmOrderService;
import garry.train.business.vo.ConfirmOrderQueryVo;
import garry.train.common.vo.PageVo;
import garry.train.common.vo.ResponseVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author Garry
 * 2024-10-07 19:53
 */
@RestController
@RequestMapping(value = "/admin/confirm-order")
public class ConfirmOrderAdminController {
    @Resource
    private ConfirmOrderService confirmOrderService;

    /**
     * 接收新增和修改确认订单的请求，如果 form.id = null，则为新增；反之为修改
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseVo save(@Valid @RequestBody ConfirmOrderDoForm form) {
        confirmOrderService.save(form);
        return ResponseVo.success();
    }

    @RequestMapping(value = "/query-list", method = RequestMethod.GET)
    public ResponseVo<PageVo<ConfirmOrderQueryVo>> queryList(@Valid ConfirmOrderQueryForm form) {
        PageVo<ConfirmOrderQueryVo> vo = confirmOrderService.queryList(form);
        return ResponseVo.success(vo);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseVo delete(@PathVariable Long id) {
        confirmOrderService.delete(id);
        return ResponseVo.success();
    }
}
