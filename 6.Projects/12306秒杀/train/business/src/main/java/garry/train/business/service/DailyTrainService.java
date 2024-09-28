package garry.train.business.service;

import garry.train.common.vo.PageVo;
import garry.train.business.form.DailyTrainQueryForm;
import garry.train.business.form.DailyTrainSaveForm;
import garry.train.business.vo.DailyTrainQueryVo;

/**
 * @author Garry
 * 2024-09-28 16:33
 */
public interface DailyTrainService {
    /**
     * 插入新每日车次，或修改已有的每日车次
     * 如果 form.id = null，则为插入；
     * 如果 form.id != null，则为修改
     */
    void save(DailyTrainSaveForm form);

    /**
     * 根据 memberId 查询所有的每日车次
     * 如果 form.memberId = null，则为管理员查询
     */
    PageVo<DailyTrainQueryVo> queryList(DailyTrainQueryForm form);

    /**
     * 根据 id(主键) 删除每日车次
     */
    void delete(Long id);
}
