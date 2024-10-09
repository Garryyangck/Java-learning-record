package garry.train.business.service;

import garry.train.business.pojo.ConfirmOrder;
import garry.train.common.vo.PageVo;
import garry.train.business.form.ConfirmOrderQueryForm;
import garry.train.business.form.ConfirmOrderDoForm;
import garry.train.business.vo.ConfirmOrderQueryVo;

import java.util.Date;
import java.util.List;

/**
 * @author Garry
 * 2024-10-07 19:53
 */
public interface ConfirmOrderService {
    /**
     * 插入新确认订单，将其状态设置为 INIT
     */
    void init(ConfirmOrderDoForm form);

    /**
     * 根据 memberId 查询所有的确认订单
     * 如果 form.memberId = null，则为管理员查询
     */
    PageVo<ConfirmOrderQueryVo> queryList(ConfirmOrderQueryForm form);

    /**
     * 根据 id(主键) 删除确认订单
     */
    void delete(Long id);

    /**
     * 处理订票操作
     */
    void doConfirm(ConfirmOrderDoForm form);

    /**
     * 用于检验用户是否会重复购买同一乘客某一确定始末站的车票
     */
    List<ConfirmOrder> queryByMemberIdAndDateAndTrainCodeAndStartAndEnd(Long memberId, Date date, String trainCode, String start, String end);
}
