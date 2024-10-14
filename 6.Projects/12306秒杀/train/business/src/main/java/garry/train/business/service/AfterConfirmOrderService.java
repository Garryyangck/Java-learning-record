package garry.train.business.service;

import garry.train.business.form.ConfirmOrderDoForm;

import java.util.List;

/**
 * @author Garry
 * 2024-10-14 13:38
 */
public interface AfterConfirmOrderService {

    /**
     * 选座成功后的事务处理，包括：
     *  daily_train_seat 修改 sell 售卖情况
     *  daily_train_ticket 修改余票数
     *  (member)ticket 增加用户购票的记录
     *  confirm_order 修改状态为成功
     *  创建 message 入库，并通过 websocket 发送给前端浏览器
     *
     * @return 处理是否成功
     */
    boolean afterDoConfirm(List<ConfirmOrderService.SeatChosen> seatChosenList, ConfirmOrderDoForm form);
}
