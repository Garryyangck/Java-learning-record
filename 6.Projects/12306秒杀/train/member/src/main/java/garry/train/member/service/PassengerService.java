package garry.train.member.service;

import garry.train.common.vo.PageVo;
import garry.train.member.form.PassengerQueryForm;
import garry.train.member.form.PassengerSaveForm;
import garry.train.member.vo.PassengerQueryVo;

/**
 * @author Garry
 * 2024-09-13 19:14
 */
public interface PassengerService {
    /**
     * 存储新乘客
     */
    void save(PassengerSaveForm form);

    /**
     * 根据 memberId 查询所有的乘客
     * 如果是管理员查询，则 form.memberId = null
     */
    PageVo<PassengerQueryVo> queryList(PassengerQueryForm form);

    /**
     * 根据 id 删除乘客
     */
    void delete(Long id);
}
