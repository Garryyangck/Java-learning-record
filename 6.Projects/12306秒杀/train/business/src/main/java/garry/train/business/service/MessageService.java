package garry.train.business.service;

import garry.train.business.vo.MessageSendVo;
import garry.train.common.vo.PageVo;
import garry.train.business.form.MessageQueryForm;
import garry.train.business.form.MessageSaveForm;
import garry.train.business.vo.MessageQueryVo;

/**
 * @author Garry
 * 2024-10-11 14:24
 */
public interface MessageService {
    /**
     * 插入新，或修改已有的
     * 如果 form.id = null，则为插入；
     * 如果 form.id != null，则为修改
     */
    void save(MessageSaveForm form);

    /**
     * 根据 memberId 查询所有的
     * 如果 form.memberId = null，则为管理员查询
     */
    PageVo<MessageQueryVo> queryList(MessageQueryForm form);

    /**
     * 根据 id(主键) 删除
     */
    void delete(Long id);

    /**
     * 通过 websocket 发送消息
     */
    void sendMessage(MessageSendVo vo, Long memberId);
}
