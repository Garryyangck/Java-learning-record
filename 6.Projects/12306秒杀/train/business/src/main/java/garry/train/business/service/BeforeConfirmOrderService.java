package garry.train.business.service;

import garry.train.business.form.ConfirmOrderDoForm;

/**
 * @author Garry
 * 2024-11-08 14:24
 */
public interface BeforeConfirmOrderService {

    /**
     * 校验验证码
     * 获取 SKToken，在此之前先获取 SKToken 的分布式锁
     * 业务数据校验，车次是否存在，车票是否存在，车次时间是否合法，
     * tickets 是否 > 0，是否有余票，同一乘客不能购买同一车次
     */
    boolean beforeDoConfirm(ConfirmOrderDoForm form, Long memberId);
}
