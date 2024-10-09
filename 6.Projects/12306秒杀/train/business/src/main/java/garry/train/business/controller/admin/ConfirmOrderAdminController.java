package garry.train.business.controller.admin;

import garry.train.business.form.ConfirmOrderQueryForm;
import garry.train.business.service.ConfirmOrderService;
import garry.train.business.vo.ConfirmOrderQueryVo;
import garry.train.common.vo.PageVo;
import garry.train.common.vo.ResponseVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Garry
 * 2024-10-07 19:53
 */
@RestController
@RequestMapping(value = "/admin/confirm-order")
public class ConfirmOrderAdminController {
    @Resource
    private ConfirmOrderService confirmOrderService;

    @RequestMapping(value = "/query-list", method = RequestMethod.GET)
    public ResponseVo<PageVo<ConfirmOrderQueryVo>> queryList(@Valid ConfirmOrderQueryForm form) {
        PageVo<ConfirmOrderQueryVo> vo = confirmOrderService.queryList(form);
        return ResponseVo.success(vo);
    }
}
