package garry.train.business.service;

import garry.train.common.vo.PageVo;
import garry.train.business.form.TrainQueryForm;
import garry.train.business.form.TrainSaveForm;
import garry.train.business.vo.TrainQueryVo;

/**
 * @author Garry
 * 2024-09-22 13:19
 */
public interface TrainService {
    /**
     * 插入新车次，或修改已有的车次
     * 如果 form.id = null，则为插入；
     * 如果 form.id != null，则为修改
     */
    void save(TrainSaveForm form);

    /**
     * 根据 memberId 查询所有的车次
     * 如果 form.memberId = null，则为管理员查询
     */
    PageVo<TrainQueryVo> queryList(TrainQueryForm form);

    /**
     * 根据 id(主键) 删除车次
     */
    void delete(Long id);
}
