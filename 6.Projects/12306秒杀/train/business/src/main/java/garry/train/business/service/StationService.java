package garry.train.business.service;

import garry.train.common.vo.PageVo;
import garry.train.business.form.StationQueryForm;
import garry.train.business.form.StationSaveForm;
import garry.train.business.vo.StationQueryVo;

/**
 * @author Garry
 * 2024-09-19 20:52
 */
public interface StationService {
    /**
     * 插入新车站，或修改已有的车站
     * 如果 form.id = null，则为插入；
     * 如果 form.id != null，则为修改
     */
    void save(StationSaveForm form);

    /**
     * 根据 memberId 查询所有的车站
     * 如果 form.memberId = null，则为管理员查询
     */
    PageVo<StationQueryVo> queryList(StationQueryForm form);

    /**
     * 根据 id(主键) 删除车站
     */
    void delete(Long id);
}
