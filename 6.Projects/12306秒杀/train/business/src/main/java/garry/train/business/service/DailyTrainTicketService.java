package garry.train.business.service;

import garry.train.business.form.DailyTrainTicketQueryForm;
import garry.train.business.form.DailyTrainTicketSaveForm;
import garry.train.business.pojo.DailyTrainTicket;
import garry.train.business.vo.DailyTrainTicketQueryVo;
import garry.train.common.vo.PageVo;

import java.util.Date;
import java.util.List;

/**
 * @author Garry
 * 2024-09-30 14:51
 */
public interface DailyTrainTicketService {
    /**
     * 插入新余票信息，或修改已有的余票信息
     * 如果 form.id = null，则为插入；
     * 如果 form.id != null，则为修改
     */
    void save(DailyTrainTicketSaveForm form);

    /**
     * 根据 memberId 查询所有的余票信息
     * 如果 form.memberId = null，则为管理员查询
     */
    PageVo<DailyTrainTicketQueryVo> queryList(DailyTrainTicketQueryForm form);

    /**
     * 根据 id(主键) 删除余票信息
     */
    void delete(Long id);

    List<DailyTrainTicket> queryByTrainCode(String trainCode);

    List<DailyTrainTicket> queryByDate(Date date);

    List<DailyTrainTicket> queryByDateAndTrainCodeAndStartAndEnd(Date date, String trainCode, String start, String end);
}
