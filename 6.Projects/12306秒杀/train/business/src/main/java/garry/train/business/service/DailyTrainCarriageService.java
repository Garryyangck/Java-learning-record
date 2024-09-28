package garry.train.business.service;

import garry.train.common.vo.PageVo;
import garry.train.business.form.DailyTrainCarriageQueryForm;
import garry.train.business.form.DailyTrainCarriageSaveForm;
import garry.train.business.vo.DailyTrainCarriageQueryVo;

/**
 * @author Garry
 * 2024-09-28 20:20
 */
public interface DailyTrainCarriageService {
    /**
     * 插入新每日车厢，或修改已有的每日车厢
     * 如果 form.id = null，则为插入；
     * 如果 form.id != null，则为修改
     */
    void save(DailyTrainCarriageSaveForm form);

    /**
     * 根据 memberId 查询所有的每日车厢
     * 如果 form.memberId = null，则为管理员查询
     */
    PageVo<DailyTrainCarriageQueryVo> queryList(DailyTrainCarriageQueryForm form);

    /**
     * 根据 id(主键) 删除每日车厢
     */
    void delete(Long id);
}
