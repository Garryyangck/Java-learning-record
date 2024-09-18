package garry.train.${module}.service;

import garry.train.common.vo.PageVo;
import garry.train.${module}.form.${Domain}QueryForm;
import garry.train.${module}.form.${Domain}SaveForm;
import garry.train.${module}.vo.${Domain}QueryVo;

/**
 * @author Garry
 * ${DateTime}
 */
public interface ${Domain}Service {
    /**
     * 插入新${tableNameCn}，或修改已有的${tableNameCn}
     * 如果 form.id = null，则为插入；
     * 如果 form.id != null，则为修改
     */
    void save(${Domain}SaveForm form);

    /**
     * 根据 memberId 查询所有的${tableNameCn}
     * 如果 form.memberId = null，则为管理员查询
     */
    PageVo<${Domain}QueryVo> queryList(${Domain}QueryForm form);

    /**
     * 根据 id(主键) 删除${tableNameCn}
     */
    void delete(Long id);
}
