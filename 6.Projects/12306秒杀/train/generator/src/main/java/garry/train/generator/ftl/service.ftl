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

    void save(${Domain}SaveForm form);

    PageVo<${Domain}QueryVo> queryList(${Domain}QueryForm form);

    void delete(Long id);
}
