package garry.train.business.service;

import garry.train.business.form.DailyTrainTicketQueryForm;
import garry.train.business.form.DailyTrainTicketSaveForm;
import garry.train.business.pojo.DailyTrainTicket;
import garry.train.business.pojo.Train;
import garry.train.business.vo.DailyTrainTicketQueryVo;
import garry.train.common.vo.PageVo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    void delete(Date date, String trainCode, String start, String end);

    /**
     * 生成每日余票
     */
    void genDaily(Date date, Train train);

    /**
     * 唯一键检验，和根据唯一键查询
     */
    List<DailyTrainTicket> queryByDateAndTrainCodeAndStartAndEnd(Date date, String trainCode, String start, String end);

    /**
     * 查出某一车次下所有的始末站的车票，以便减少它们的余票
     */
    List<DailyTrainTicket> queryByDateAndTrainCode(Date date, String trainCode);

    /**
     * 获取 DailyTrainTicket 各个座位类型的余票的 map
     * Map<String, Integer> => SeatTypeCode => RemainingNumber
     */
    static Map<String, Integer> getSeatCodeRemainNumMap(DailyTrainTicket dailyTrainTicket) {
        HashMap<String, Integer> seatCodeRemainNumMap = new HashMap<>();
        seatCodeRemainNumMap.put("1", dailyTrainTicket.getYdz());
        seatCodeRemainNumMap.put("2", dailyTrainTicket.getEdz());
        seatCodeRemainNumMap.put("3", dailyTrainTicket.getRw());
        seatCodeRemainNumMap.put("4", dailyTrainTicket.getYw());
        return seatCodeRemainNumMap;
    }
}
