package garry.train.business.controller;

import garry.train.business.form.ConfirmOrderDoForm;
import garry.train.business.service.ConfirmOrderService;
import garry.train.common.vo.ResponseVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Garry
 * 2024-10-07 19:53
 */
@RestController
@RequestMapping(value = "/confirm-order")
public class ConfirmOrderController {
    @Resource
    private ConfirmOrderService confirmOrderService;

    @RequestMapping(value = "/do", method = RequestMethod.POST)
    public ResponseVo doConfirm(@Valid @RequestBody ConfirmOrderDoForm form) {
        confirmOrderService.doConfirm(form);
        return ResponseVo.success();
    }
}
