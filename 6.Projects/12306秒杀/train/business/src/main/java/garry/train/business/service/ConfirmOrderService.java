package garry.train.business.service;

import garry.train.common.vo.PageVo;
import garry.train.business.form.ConfirmOrderQueryForm;
import garry.train.business.form.ConfirmOrderDoForm;
import garry.train.business.vo.ConfirmOrderQueryVo;

/**
 * @author Garry
 * 2024-10-07 19:53
 */
public interface ConfirmOrderService {
    /**
     * 插入新确认订单，或修改已有的确认订单
     * 如果 form.id = null，则为插入；
     * 如果 form.id != null，则为修改
     */
    void save(ConfirmOrderDoForm form);

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
}
