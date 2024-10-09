package garry.train.business.service;

import garry.train.business.pojo.DailyTrainSeat;
import garry.train.business.pojo.Train;
import garry.train.common.vo.PageVo;
import garry.train.business.form.DailyTrainSeatQueryForm;
import garry.train.business.form.DailyTrainSeatSaveForm;
import garry.train.business.vo.DailyTrainSeatQueryVo;

import java.util.Date;
import java.util.List;

/**
 * @author Garry
 * 2024-10-09 00:00
 */
public interface DailyTrainSeatService {
    /**
     * 插入新每日座位，或修改已有的每日座位
     * 如果 form.id = null，则为插入；
     * 如果 form.id != null，则为修改
     */
    void save(DailyTrainSeatSaveForm form);

    /**
     * 根据 memberId 查询所有的每日座位
     * 如果 form.memberId = null，则为管理员查询
     */
    PageVo<DailyTrainSeatQueryVo> queryList(DailyTrainSeatQueryForm form);

    /**
     * 根据 id(主键) 删除每日座位
     */
    void delete(Long id);

    /**
     * 生成 date 日，train 下的所有 seat
     */
    void genDaily(Date date, Train train);

    /**
     * 一次性查出 date, trainCode 下的所有座位，之后再通过 stream 进行筛选，减少查数据库的次数
     */
    List<DailyTrainSeat> queryByDateAndTrainCode(Date date, String trainCode);
}
