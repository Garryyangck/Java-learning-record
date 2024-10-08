package garry.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import garry.train.common.util.CommonUtil;
import garry.train.common.vo.PageVo;
import garry.train.business.form.ConfirmOrderQueryForm;
import garry.train.business.form.ConfirmOrderDoForm;
import garry.train.business.mapper.ConfirmOrderMapper;
import garry.train.business.pojo.ConfirmOrder;
import garry.train.business.pojo.ConfirmOrderExample;
import garry.train.business.service.ConfirmOrderService;
import garry.train.business.vo.ConfirmOrderQueryVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Garry
 * 2024-10-07 19:53
 */
@Slf4j
@Service
public class ConfirmOrderServiceImpl implements ConfirmOrderService {
    @Resource
    private ConfirmOrderMapper confirmOrderMapper;

    @Override
    public void save(ConfirmOrderDoForm form) {
        ConfirmOrder confirmOrder = BeanUtil.copyProperties(form, ConfirmOrder.class);
        DateTime now = DateTime.now();

        if (ObjectUtil.isNull(confirmOrder.getId())) { // 插入
            // 插入时要看数据库有没有唯一键约束，在此校验唯一键约束，防止出现 DuplicationKeyException

            // 对Id、createTime、updateTime 重新赋值
            // 可能还需要重新赋值其它的字段，比如 Passenger.memberId
            confirmOrder.setId(CommonUtil.getSnowflakeNextId());
            confirmOrder.setCreateTime(now);
            confirmOrder.setUpdateTime(now);
            confirmOrderMapper.insert(confirmOrder);
            log.info("插入确认订单：{}", confirmOrder);
        } else { // 修改
            confirmOrder.setUpdateTime(now);
            confirmOrderMapper.updateByPrimaryKeySelective(confirmOrder);
            log.info("修改确认订单：{}", confirmOrder);
        }
    }

    @Override
    public PageVo<ConfirmOrderQueryVo> queryList(ConfirmOrderQueryForm form) {
        ConfirmOrderExample confirmOrderExample = new ConfirmOrderExample();
        confirmOrderExample.setOrderByClause("update_time desc"); // 最新更新的数据，最先被查出来
        ConfirmOrderExample.Criteria criteria = confirmOrderExample.createCriteria();
        // 这里自定义一些过滤的条件，比如:
//        // 用户只能查自己 memberId 下的确认订单
//        if (ObjectUtil.isNotNull()) {
//            criteria.andMemberIdEqualTo(memberId);
//        }

        // 启动分页
        PageHelper.startPage(form.getPageNum(), form.getPageSize());

        // 获取 confirmOrders
        List<ConfirmOrder> confirmOrders = confirmOrderMapper.selectByExample(confirmOrderExample);

        // 获得 pageInfo 对象，并将其 List 的模板类型改为 ConfirmOrderQueryVo
        // 注意这里必须先获取 pageInfo，再尝试获取 List<ConfirmOrderQueryVo>，否则无法正确获取 pageNum，pages 等重要属性
        PageInfo<ConfirmOrder> pageInfo = new PageInfo<>(confirmOrders);
        List<ConfirmOrderQueryVo> voList = BeanUtil.copyToList(pageInfo.getList(), ConfirmOrderQueryVo.class);

        // 获取 PageVo 对象
        PageVo<ConfirmOrderQueryVo> vo = BeanUtil.copyProperties(pageInfo, PageVo.class);
        vo.setList(voList);
        vo.setMsg("查询确认订单列表成功");
        return vo;
    }

    @Override
    public void delete(Long id) {
        confirmOrderMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void doConfirm(ConfirmOrderDoForm form) {
        // 业务数据校验，车次是否存在，车票是否存在，是否有余票，车次时间是否合法，tickets 是否 > 0，同一乘客不能购买同一车次

        // 创建对象，插入 confirm_order 表，状态为初始

        // 扣减余票，判断余票是否足够 (伪减少库存，并没有真的写入)

        // 选座 (当然，要先根据 tickets 判断用户是否要选座)

            // 遍历每一个车厢，首先找到 SeatType 符合的车厢

                // 按座位顺序 (index) 遍历车厢的座位，先找到第一个 SeatCol.code 正确的座位
                // 然后判断其 start ~ end 是否被卖出 TODO 为了提高效率，把 sell 换为 int 类型，进行二进制数的位运算进行判断
                // 没有卖出则判断是否选 > 1 个座位，如果是则寻找从当前座位开始的两排内，是否满足所有选座条件
                // 不满足则返回第一个正确座位的 index，继续向后遍历，直到找到新的正确座位，或者遍历完该车厢

        // 遍历完所有车厢也没有找到合适选座，或不选座，则自动分配座位 (同一车厢，顺序分配未卖出的座位)

        // 选完所有座位后 trx (事务) 处理

            // daily_train_seat 修改 sell 售卖情况
            // daily_train_ticket 修改余票数
            // (member)ticket 增加用户购票的记录
            // confirm_order 修改状态为成功
    }
}
