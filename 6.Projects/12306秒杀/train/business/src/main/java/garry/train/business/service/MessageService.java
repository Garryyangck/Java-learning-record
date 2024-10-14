package garry.train.business.service;

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
     * 根据 id(主键) 修改状态
     */
    void delete(Long id);

    /**
     * 给 websocket 发送消息，通知用户选票成功、未读消息数，并创建 Message 对象存入数据库
     */
    void sendSystemMessage(Long memberId, String content);

    /**
     * 查询用户的未读消息数
     */
    int getUnreadNum(Long toId);

    /**
     * 修改状态为已读
     */
    int read(Long id, Long memberId);

    /**
     * 修改状态为置顶
     */
    void top(Long id);

    /**
     * 取消置顶
     */
    void untop(Long id);
}
