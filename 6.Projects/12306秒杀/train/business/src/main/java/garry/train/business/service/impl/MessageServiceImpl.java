package garry.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import garry.train.business.enums.MessageStatusEnum;
import garry.train.business.enums.MessageTypeEnum;
import garry.train.business.form.MessageQueryForm;
import garry.train.business.form.MessageSaveForm;
import garry.train.business.mapper.MessageMapper;
import garry.train.business.pojo.Message;
import garry.train.business.pojo.MessageExample;
import garry.train.business.service.MessageService;
import garry.train.business.vo.MessageQueryVo;
import garry.train.business.vo.MessageSendVo;
import garry.train.business.websocket.WebSocketClient;
import garry.train.common.consts.CommonConst;
import garry.train.common.enums.ResponseEnum;
import garry.train.common.exception.BusinessException;
import garry.train.common.util.CommonUtil;
import garry.train.common.vo.PageVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Garry
 * 2024-10-11 14:24
 */
@Slf4j
@Service
public class MessageServiceImpl implements MessageService {
    @Resource
    private MessageMapper messageMapper;

    @Resource
    private WebSocketClient webSocketClient;

    @Override
    public void save(MessageSaveForm form) {
        Message message = BeanUtil.copyProperties(form, Message.class);
        DateTime now = DateTime.now();

        if (ObjectUtil.isNull(message.getId())) { // 插入
            // 插入时要看数据库有没有唯一键约束，在此校验唯一键约束，防止出现 DuplicationKeyException

            // 对Id、createTime、updateTime 重新赋值
            // 可能还需要重新赋值其它的字段，比如 Passenger.memberId
            message.setId(CommonUtil.getSnowflakeNextId());
            message.setCreateTime(now);
            message.setUpdateTime(now);
            messageMapper.insert(message);
            log.info("插入消息：{}", message);
        } else { // 修改
            message.setUpdateTime(now);
            messageMapper.updateByPrimaryKeySelective(message);
            log.info("修改消息：{}", message);
        }
    }

    @Override
    public PageVo<MessageQueryVo> queryList(MessageQueryForm form) {
        MessageExample messageExample = new MessageExample();
        messageExample.setOrderByClause("update_time desc"); // 最新更新的数据，最先被查出来
        MessageExample.Criteria criteria = messageExample.createCriteria();
        // 用户只能查自己 memberId 下的
        if (ObjectUtil.isNotNull(form.getToId())) {
            criteria.andToIdEqualTo(form.getToId())
                    .andStatusNotEqualTo(MessageStatusEnum.DELETE.getCode());
        }

        // 启动分页
        PageHelper.startPage(form.getPageNum(), form.getPageSize());

        // 获取 messages
        List<Message> messages = messageMapper.selectByExample(messageExample);

        // 获得 pageInfo 对象，并将其 List 的模板类型改为 MessageQueryVo
        // 注意这里必须先获取 pageInfo，再尝试获取 List<MessageQueryVo>，否则无法正确获取 pageNum，pages 等重要属性
        PageInfo<Message> pageInfo = new PageInfo<>(messages);
        List<MessageQueryVo> voList = BeanUtil.copyToList(pageInfo.getList(), MessageQueryVo.class);

        // 获取 PageVo 对象
        PageVo<MessageQueryVo> vo = BeanUtil.copyProperties(pageInfo, PageVo.class);
        vo.setList(voList);
        vo.setMsg("查询消息列表成功");
        return vo;
    }

    @Override
    public void delete(Long id) {
        Message message = messageMapper.selectByPrimaryKey(id);
        message.setStatus(MessageStatusEnum.DELETE.getCode());
        messageMapper.updateByPrimaryKeySelective(message);
    }

    @Override
    public void sendSystemMessage(Long memberId, String content) {
        // 创建 message 对象持久化
        MessageSaveForm saveForm = new MessageSaveForm();
        saveForm.setFromId(CommonConst.SystemId);
        saveForm.setToId(memberId);
        saveForm.setType(MessageTypeEnum.SYSTEM_MESSAGE.getCode());
        saveForm.setContent(content);
        saveForm.setStatus(MessageStatusEnum.UNREAD.getCode());
        save(saveForm);

        // 获取未读消息数
        MessageSendVo sendVo = BeanUtil.copyProperties(saveForm, MessageSendVo.class);
        sendVo.setUnreadNum(getUnreadNum(memberId));

        // 给 websocket 服务器发送消息
        try {
            webSocketClient.sendMessage(sendVo);
        } catch (Exception e) {
            throw new BusinessException(ResponseEnum.BUSINESS_WEBSOCKET_MESSAGE_SEND_FAILED);
        }
    }

    @Override
    public int getUnreadNum(Long toId) {
        MessageExample messageExample = new MessageExample();
        messageExample.createCriteria()
                .andToIdEqualTo(toId)
                .andStatusEqualTo(MessageStatusEnum.UNREAD.getCode());
        return messageMapper.selectByExample(messageExample).size();
    }

    @Override
    public void read(Long id) {
        Message message = messageMapper.selectByPrimaryKey(id);
        message.setStatus(MessageStatusEnum.READ.getCode());
        messageMapper.updateByPrimaryKeySelective(message);
    }
}
