package garry.train.business.service;

import garry.train.common.vo.PageVo;
import garry.train.business.form.TrainCarriageQueryForm;
import garry.train.business.form.TrainCarriageSaveForm;
import garry.train.business.vo.TrainCarriageQueryVo;

/**
 * @author Garry
 * 2024-09-22 20:48
 */
public interface TrainCarriageService {
    /**
     * 插入新火车车厢，或修改已有的火车车厢
     * 如果 form.id = null，则为插入；
     * 如果 form.id != null，则为修改
     */
    void save(TrainCarriageSaveForm form);

    /**
     * 根据 memberId 查询所有的火车车厢
     * 如果 form.memberId = null，则为管理员查询
     */
    PageVo<TrainCarriageQueryVo> queryList(TrainCarriageQueryForm form);

    /**
     * 根据 id(主键) 删除火车车厢
     */
    void delete(Long id);
}
