package garry.train.business.service;

import garry.train.common.vo.PageVo;
import garry.train.business.form.SkTokenQueryForm;
import garry.train.business.form.SkTokenSaveForm;
import garry.train.business.vo.SkTokenQueryVo;

/**
 * @author Garry
 * 2024-11-05 18:16
 */
public interface SkTokenService {
    /**
     * 插入新秒杀令牌，或修改已有的秒杀令牌
     * 如果 form.id = null，则为插入；
     * 如果 form.id != null，则为修改
     */
    void save(SkTokenSaveForm form);

    /**
     * 根据 memberId 查询所有的秒杀令牌
     * 如果 form.memberId = null，则为管理员查询
     */
    PageVo<SkTokenQueryVo> queryList(SkTokenQueryForm form);

    /**
     * 根据 id(主键) 删除秒杀令牌
     */
    void delete(Long id);
}
