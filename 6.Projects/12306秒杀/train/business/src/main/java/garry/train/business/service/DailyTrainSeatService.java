package garry.train.business.service;

import garry.train.business.pojo.Train;
import garry.train.common.vo.PageVo;
import garry.train.business.form.DailyTrainSeatQueryForm;
import garry.train.business.form.DailyTrainSeatSaveForm;
import garry.train.business.vo.DailyTrainSeatQueryVo;

import java.util.Date;

/**
 * @author Garry
 * 2024-09-28 20:53
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
}
