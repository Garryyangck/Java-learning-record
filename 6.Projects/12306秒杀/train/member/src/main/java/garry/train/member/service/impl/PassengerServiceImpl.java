package garry.train.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
    public PageInfo<PassengerQueryVo> queryList(PassengerQueryForm form) {
        Long memberId = form.getMemberId();
        PassengerExample passengerExample = new PassengerExample();
        PassengerExample.Criteria criteria = passengerExample.createCriteria();

        // 只有用户，才只能查自己 memberId 下的乘客
        if (ObjectUtil.isNotNull(memberId)) {
            criteria.andMemberIdEqualTo(memberId);
        }

        // 启动分页
        PageHelper.startPage(1, 2);

        // 获取 passengers，通过 stream 转为 voList
        List<Passenger> passengers = passengerMapper.selectByExample(passengerExample);
        List<PassengerQueryVo> voList = BeanUtil.copyToList(passengers, PassengerQueryVo.class);

        // 调用接口，将 list 封装为 pageInfo 对象
        PageInfo<PassengerQueryVo> pageInfo = new PageInfo<>(voList);
        return pageInfo;
    }
}
