package garry.train.business.service;

import garry.train.business.form.DailyTrainStationQueryForm;
import garry.train.business.form.DailyTrainStationSaveForm;
import garry.train.business.pojo.DailyTrainStation;
import garry.train.business.pojo.Train;
import garry.train.business.vo.DailyTrainStationQueryVo;
import garry.train.common.vo.PageVo;

import java.util.Date;
import java.util.List;

/**
 * @author Garry
 * 2024-09-28 19:32
 */
public interface DailyTrainStationService {
    /**
     * 插入新每日车站，或修改已有的每日车站
     * 如果 form.id = null，则为插入；
     * 如果 form.id != null，则为修改
     */
    void save(DailyTrainStationSaveForm form);

    /**
     * 根据 memberId 查询所有的每日车站
     * 如果 form.memberId = null，则为管理员查询
     */
    PageVo<DailyTrainStationQueryVo> queryList(DailyTrainStationQueryForm form);

    /**
     * 根据 id(主键) 删除每日车站
     */
    void delete(Long id);

    /**
     * 生成 date 日，train 下的所有 station
     */
    void genDaily(Date date, Train train);

    /**
     * 校验唯一键
     */
    List<DailyTrainStation> queryByDateAndTrainCodeAndIndex(Date date, String trainCode, Integer index);

    /**
     * 校验唯一键
     */
    List<DailyTrainStation> queryByDateAndTrainCodeAndName(Date date, String trainCode, String name);
}
