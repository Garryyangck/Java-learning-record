package garry.train.member.service;

import garry.train.common.vo.PageVo;
import garry.train.member.form.${Domain}QueryForm;
import garry.train.member.form.${Domain}SaveForm;
import garry.train.member.vo.${Domain}QueryVo;

/**
 * @author Garry
 * ${DateTime}
 */
public interface ${Domain}Service {
    /**
     * 存储新乘客
     */
    void save(${Domain}SaveForm form);

    /**
     * 根据 memberId 查询所有的乘客
     * 如果是管理员查询，则 form.memberId = null
     */
    PageVo<${Domain}QueryVo> queryList(${Domain}QueryForm form);

    /**
     * 根据 id 删除乘客
     */
    void delete(Long id);
}
