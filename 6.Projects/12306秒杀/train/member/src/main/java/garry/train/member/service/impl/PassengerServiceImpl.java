package garry.train.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import garry.train.common.util.CommonUtil;
import garry.train.common.util.HostHolder;
import garry.train.member.form.PassengerSaveForm;
import garry.train.member.mapper.PassengerMapper;
import garry.train.member.pojo.Passenger;
import garry.train.member.service.PassengerService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
