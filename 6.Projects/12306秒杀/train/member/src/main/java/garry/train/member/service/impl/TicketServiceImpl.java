package garry.train.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import garry.train.common.util.CommonUtil;
import garry.train.common.vo.PageVo;
import garry.train.member.form.TicketQueryForm;
import garry.train.member.form.TicketSaveForm;
import garry.train.member.mapper.TicketMapper;
import garry.train.member.pojo.Ticket;
import garry.train.member.pojo.TicketExample;
import garry.train.member.service.TicketService;
import garry.train.member.vo.TicketQueryVo;
import io.seata.core.context.RootContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Garry
 * 2024-10-14 15:19
 */
@Slf4j
@Service
public class TicketServiceImpl implements TicketService {
    @Resource
    private TicketMapper ticketMapper;

    @Override
    public void save(TicketSaveForm form) {
        log.info("Seata 的全局事务 ID = {} (只有分布式事务生效时才会打印)", RootContext.getXID());

        Ticket ticket = BeanUtil.copyProperties(form, Ticket.class);
        DateTime now = DateTime.now();

        if (ObjectUtil.isNull(ticket.getId())) { // 插入
            // 插入时要看数据库有没有唯一键约束，在此校验唯一键约束，防止出现 DuplicationKeyException

            // 对Id、createTime、updateTime 重新赋值
            // 可能还需要重新赋值其它的字段，比如 Passenger.memberId
            ticket.setId(CommonUtil.getSnowflakeNextId());
            ticket.setCreateTime(now);
            ticket.setUpdateTime(now);
            ticketMapper.insert(ticket);
            log.info("插入车票：{}", ticket);
        } else { // 修改
            ticket.setUpdateTime(now);
            ticketMapper.updateByPrimaryKeySelective(ticket);
            log.info("修改车票：{}", ticket);
        }
    }

    @Override
    public PageVo<TicketQueryVo> queryList(TicketQueryForm form) {
        TicketExample ticketExample = new TicketExample();
        ticketExample.setOrderByClause("update_time desc"); // 最新更新的数据，最先被查出来
        TicketExample.Criteria criteria = ticketExample.createCriteria();
        // 用户只能查自己 memberId 下的车票
        if (ObjectUtil.isNotNull(form.getMemberId())) {
            criteria.andMemberIdEqualTo(form.getMemberId());
        }

        // 启动分页
        PageHelper.startPage(form.getPageNum(), form.getPageSize());

        // 获取 tickets
        List<Ticket> tickets = ticketMapper.selectByExample(ticketExample);

        // 获得 pageInfo 对象，并将其 List 的模板类型改为 TicketQueryVo
        // 注意这里必须先获取 pageInfo，再尝试获取 List<TicketQueryVo>，否则无法正确获取 pageNum，pages 等重要属性
        PageInfo<Ticket> pageInfo = new PageInfo<>(tickets);
        List<TicketQueryVo> voList = BeanUtil.copyToList(pageInfo.getList(), TicketQueryVo.class);

        // 获取 PageVo 对象
        PageVo<TicketQueryVo> vo = BeanUtil.copyProperties(pageInfo, PageVo.class);
        vo.setList(voList);
        vo.setMsg("查询车票列表成功");
        return vo;
    }

    @Override
    public void delete(Long id) {
        ticketMapper.deleteByPrimaryKey(id);
    }
}
