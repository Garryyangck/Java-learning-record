package garry.train.member.service;

import garry.train.common.vo.PageVo;
import garry.train.member.form.PassengerQueryForm;
import garry.train.member.form.PassengerSaveForm;
import garry.train.member.vo.PassengerQueryVo;

/**
 * @author Garry
 * 2024-09-18 16:17
 */
public interface PassengerService {
    /**
     * 插入新乘车人，或修改已有的乘车人
     * 如果 form.id = null，则为插入；
     * 如果 form.id != null，则为修改
     */
    void save(PassengerSaveForm form);

    /**
     * 根据 memberId 查询所有的乘车人
     * 如果 form.memberId = null，则为管理员查询
     */
    PageVo<PassengerQueryVo> queryList(PassengerQueryForm form);

    /**
     * 根据 id(主键) 删除乘车人
     */
    void delete(Long id);
}
