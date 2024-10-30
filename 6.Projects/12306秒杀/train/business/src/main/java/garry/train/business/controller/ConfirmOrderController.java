package garry.train.business.controller;

import garry.train.business.form.ConfirmOrderDoForm;
import garry.train.business.service.ConfirmOrderService;
import garry.train.common.enums.ResponseEnum;
import garry.train.common.exception.BusinessException;
import garry.train.common.util.HostHolder;
import garry.train.common.vo.ResponseVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.slf4j.MDC;
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

    @Resource
    private HostHolder hostHolder;

    @RequestMapping(value = "/do", method = RequestMethod.POST)
    public ResponseVo doConfirm(@Valid @RequestBody ConfirmOrderDoForm form) {
        form.setMemberId(hostHolder.getMemberId());
        if (!confirmOrderService.checkConfirmOrder(form)) {
            throw new BusinessException(ResponseEnum.BUSINESS_CONFIRM_ORDER_CHECK_FAILED);
        }
        confirmOrderService.doConfirm(form, MDC.get("LOG_ID"));
        return ResponseVo.success();
    }
}
