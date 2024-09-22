package garry.train.business.service;

import garry.train.common.vo.PageVo;
import garry.train.business.form.TrainStationQueryForm;
import garry.train.business.form.TrainStationSaveForm;
import garry.train.business.vo.TrainStationQueryVo;

/**
 * @author Garry
 * 2024-09-22 14:22
 */
public interface TrainStationService {
    /**
     * 插入新火车车站，或修改已有的火车车站
     * 如果 form.id = null，则为插入；
     * 如果 form.id != null，则为修改
     */
    void save(TrainStationSaveForm form);

    /**
     * 根据 memberId 查询所有的火车车站
     * 如果 form.memberId = null，则为管理员查询
     */
    PageVo<TrainStationQueryVo> queryList(TrainStationQueryForm form);

    /**
     * 根据 id(主键) 删除火车车站
     */
    void delete(Long id);
}
