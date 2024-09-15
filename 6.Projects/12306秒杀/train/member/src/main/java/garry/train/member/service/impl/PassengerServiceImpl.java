package garry.train.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import garry.train.common.util.CommonUtil;
import garry.train.common.util.HostHolder;
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
 * 2024-09-13 19:14
 */

@Slf4j
@Service
public class PassengerServiceImpl implements PassengerService {
    @Resource
    private PassengerMapper passengerMapper;

    @Resource
    private HostHolder hostHolder;

    @Override
    public void save(PassengerSaveForm form) {
        Passenger passenger = BeanUtil.copyProperties(form, Passenger.class);
        // 对Id、memberId、createTime、updateTime 重新赋值
        passenger.setId(CommonUtil.getSnowflakeNextId());
        passenger.setMemberId(hostHolder.getMemberId());
        DateTime now = DateTime.now();
        passenger.setCreateTime(now);
        passenger.setUpdateTime(now);
        passengerMapper.insert(passenger);
    }

    @Override
    public List<PassengerQueryVo> queryList(PassengerQueryForm form) {
        List<Passenger> passengers = selectByMemberId(form.getMemberId());
        return BeanUtil.copyToList(passengers, PassengerQueryVo.class);
    }

    /**
     * memberId 为空，则返回全部
     */
    private List<Passenger> selectByMemberId(Long memberId) {
        PassengerExample passengerExample = new PassengerExample();
        PassengerExample.Criteria criteria = passengerExample.createCriteria();
        if (ObjectUtil.isNotNull(memberId)) { // 用户
            criteria.andMemberIdEqualTo(memberId);
        }
        return passengerMapper.selectByExample(passengerExample);
    }
}
