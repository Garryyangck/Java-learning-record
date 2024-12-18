package garry.train.business.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import garry.train.business.form.ConfirmOrderDoForm;
import garry.train.business.service.BeforeConfirmOrderService;
import garry.train.common.consts.CommonConst;
import garry.train.common.enums.ResponseEnum;
import garry.train.common.exception.BusinessException;
import garry.train.common.util.HostHolder;
import garry.train.common.vo.ResponseVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Garry
 * 2024-10-07 19:53
 */
@Slf4j
@RestController
@RequestMapping(value = "/confirm-order")
public class ConfirmOrderController {
    @Resource
    private BeforeConfirmOrderService beforeConfirmOrderService;

    @Resource
    private HostHolder hostHolder;

    @SentinelResource(value = "confirmOrderDo", blockHandler = "doConfirmBlock")
    @RequestMapping(value = "/do", method = RequestMethod.POST)
    public ResponseVo doConfirm(@Valid @RequestBody ConfirmOrderDoForm form) {
        form.setMemberId(hostHolder.getMemberId());
        form.setLOG_ID(MDC.get(CommonConst.LOG_ID));
        if (!beforeConfirmOrderService.beforeDoConfirm(form, hostHolder.getMemberId())) {
            throw new BusinessException(ResponseEnum.BUSINESS_CONFIRM_ORDER_CHECK_FAILED);
        }
        return ResponseVo.success();
    }

    public ResponseVo doConfirmBlock(ConfirmOrderDoForm form, BlockException e) {
        log.info("{} 的 /confirm-order/do 接口的请求被降级", form.getMemberId());
        throw new BusinessException(ResponseEnum.BUSINESS_CONFIRM_ORDER_SENTINEL_BLOCKED);
    }
}
