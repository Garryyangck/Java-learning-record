package garry.train.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import garry.train.common.consts.CommonConst;
import garry.train.common.enums.ResponseEnum;
import garry.train.common.exception.BusinessException;
import garry.train.common.util.CommonUtil;
import garry.train.common.vo.PageVo;
import garry.train.member.form.PassengerQueryForm;
import garry.train.member.form.PassengerSaveForm;
import garry.train.member.mapper.PassengerMapper;
import garry.train.member.pojo.Passenger;
import garry.train.member.pojo.PassengerExample;
import garry.train.member.service.PassengerService;
import garry.train.member.vo.PassengerQueryVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Garry
 * 2024-09-18 16:17
 */
@Slf4j
@Service
public class PassengerServiceImpl implements PassengerService {
    @Resource
    private PassengerMapper passengerMapper;

    @Override
    public void save(PassengerSaveForm form) {
        Passenger passenger = BeanUtil.copyProperties(form, Passenger.class);
        DateTime now = DateTime.now();

        if (ObjectUtil.isNull(passenger.getId())) { // 插入
            // 对Id、memberId、createTime、updateTime 重新赋值
            passenger.setId(CommonUtil.getSnowflakeNextId());
            passenger.setMemberId(form.getMemberId()); // 用户在 Controller 直接 hostHolder 获取 memberId；管理员则是输入用户 memberId
            passenger.setCreateTime(now);
            passenger.setUpdateTime(now);

            // 持有的乘客总数不能超过 CommonConst.PASSENGER_LIMIT
            if (countByMemberId(passenger.getMemberId()) > CommonConst.PASSENGER_LIMIT) {
                throw new BusinessException(ResponseEnum.MEMBER_EXCEED_PASSENGER_LIMIT);
            }

            passengerMapper.insert(passenger);
            log.info("插入乘车人：{}", passenger);
        } else { // 修改
            passenger.setUpdateTime(now);
            passengerMapper.updateByPrimaryKeySelective(passenger);
            log.info("修改乘车人：{}", passenger);
        }
    }

    @Override
    public PageVo<PassengerQueryVo> queryList(PassengerQueryForm form) {
        Long memberId = form.getMemberId();
        PassengerExample passengerExample = new PassengerExample();
        passengerExample.setOrderByClause("name desc");
        PassengerExample.Criteria criteria = passengerExample.createCriteria();

        // 用户只能查自己 memberId 下的乘车人
        if (ObjectUtil.isNotNull(memberId)) {
            criteria.andMemberIdEqualTo(memberId);
        }

        // 启动分页
        PageHelper.startPage(form.getPageNum(), form.getPageSize());

        // 获取 passengers
        List<Passenger> passengers = passengerMapper.selectByExample(passengerExample);

        // 获得 pageInfo 对象，并将其 List 的模板类型改为 PassengerQueryVo
        // 注意这里必须先获取 pageInfo，再尝试获取 List<PassengerQueryVo>，否则无法正确获取 pageNum，pages 等重要属性
        PageInfo<Passenger> pageInfo = new PageInfo<>(passengers);
        List<PassengerQueryVo> voList = BeanUtil.copyToList(pageInfo.getList(), PassengerQueryVo.class);

        // 获取 PageVo 对象
        PageVo<PassengerQueryVo> vo = BeanUtil.copyProperties(pageInfo, PageVo.class);
        vo.setList(voList);
        vo.setMsg("查询乘车人列表成功");
        return vo;
    }

    @Override
    public void delete(Long id) {
        passengerMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int countByMemberId(Long memberId) {
        PassengerExample passengerExample = new PassengerExample();
        passengerExample.createCriteria()
                .andMemberIdEqualTo(memberId);
        return (int) passengerMapper.countByExample(passengerExample);
    }

    @Override
    public List<PassengerQueryVo> queryAll(Long memberId) {
        PassengerExample passengerExample = new PassengerExample();
        passengerExample.createCriteria()
                .andMemberIdEqualTo(memberId);
        passengerExample.setOrderByClause("name");
        return BeanUtil.copyToList(passengerMapper.selectByExample(passengerExample), PassengerQueryVo.class);
    }
}
