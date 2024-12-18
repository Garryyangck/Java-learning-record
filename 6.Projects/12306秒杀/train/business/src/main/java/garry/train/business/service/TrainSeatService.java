package garry.train.business.service;

import garry.train.business.form.TrainSeatQueryForm;
import garry.train.business.form.TrainSeatSaveForm;
import garry.train.business.pojo.TrainSeat;
import garry.train.business.vo.TrainSeatQueryVo;
import garry.train.common.vo.PageVo;

import java.util.List;

/**
 * @author Garry
 * 2024-09-22 21:13
 */
public interface TrainSeatService {
    /**
     * 插入新座位，或修改已有的座位
     * 如果 form.id = null，则为插入；
     * 如果 form.id != null，则为修改
     */
    void save(TrainSeatSaveForm form);

    /**
     * 根据 memberId 查询所有的座位
     * 如果 form.memberId = null，则为管理员查询
     */
    PageVo<TrainSeatQueryVo> queryList(TrainSeatQueryForm form);

    /**
     * 根据 id(主键) 删除座位
     */
    void delete(Long id);

    /**
     * 根据 trainCode 的车厢的 seatType 生成座位
     */
    void genTrainSeat(String trainCode);

    /**
     * 根据 trainCode 删除座位，主要被 service 层调用
     */
    void deleteByTrainCode(String trainCode);

    /**
     * 查找 trainCode 车次下，第 carriageIndex 节车厢下的所有座位
     * 主要用于 TrainCarriageService.delete 中删除该车次、该车厢下的所有座位
     */
    List<TrainSeat> queryByTrainCodeAndCarriageIndex(String trainCode, Integer carriageIndex);

    /**
     * service 间调用
     */
    List<TrainSeat> queryByTrainCode(String trainCode);
}
