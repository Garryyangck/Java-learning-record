package garry.train.member.service;

import garry.train.common.vo.PageVo;
import garry.train.member.form.TicketQueryForm;
import garry.train.member.form.TicketSaveForm;
import garry.train.member.vo.TicketQueryVo;

/**
 * @author Garry
 * 2024-09-18 16:17
 */
public interface TicketService {
    /**
     * 插入新车票，或修改已有的车票
     * 如果 form.id = null，则为插入；
     * 如果 form.id != null，则为修改
     */
    void save(TicketSaveForm form);

    /**
     * 根据 memberId 查询所有的车票
     * 如果 form.memberId = null，则为管理员查询
     */
    PageVo<TicketQueryVo> queryList(TicketQueryForm form);

    /**
     * 根据 id(主键) 删除车票
     */
    void delete(Long id);
}
