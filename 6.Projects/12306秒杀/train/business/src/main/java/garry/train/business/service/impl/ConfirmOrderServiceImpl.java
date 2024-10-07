package garry.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import garry.train.common.util.CommonUtil;
import garry.train.common.vo.PageVo;
import garry.train.business.form.ConfirmOrderQueryForm;
import garry.train.business.form.ConfirmOrderSaveForm;
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
    public void save(ConfirmOrderSaveForm form) {
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
}
