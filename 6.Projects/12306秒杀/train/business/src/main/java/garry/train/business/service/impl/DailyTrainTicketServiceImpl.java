package garry.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import garry.train.common.enums.ResponseEnum;
import garry.train.common.exception.BusinessException;
import garry.train.common.util.CommonUtil;
import garry.train.common.vo.PageVo;
import garry.train.business.form.DailyTrainTicketQueryForm;
import garry.train.business.form.DailyTrainTicketSaveForm;
import garry.train.business.mapper.DailyTrainTicketMapper;
import garry.train.business.pojo.DailyTrainTicket;
import garry.train.business.pojo.DailyTrainTicketExample;
import garry.train.business.service.DailyTrainTicketService;
import garry.train.business.vo.DailyTrainTicketQueryVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScannerRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Garry
 * 2024-09-30 14:51
 */
@Slf4j
@Service
public class DailyTrainTicketServiceImpl implements DailyTrainTicketService {
    @Resource
    private DailyTrainTicketMapper dailyTrainTicketMapper;
    @Autowired
    private MapperScannerRegistrar mapperScannerRegistrar;

    @Override
    public void save(DailyTrainTicketSaveForm form) {
        DailyTrainTicket dailyTrainTicket = BeanUtil.copyProperties(form, DailyTrainTicket.class);
        DateTime now = DateTime.now();

        if (ObjectUtil.isNull(dailyTrainTicket.getId())) { // 插入
            // 插入时要看数据库有没有唯一键约束，在此校验唯一键约束，防止出现 DuplicationKeyException
            if (!queryByDateAndTrainCodeAndStartAndEnd(dailyTrainTicket.getDate(), dailyTrainTicket.getTrainCode(), dailyTrainTicket.getStart(), dailyTrainTicket.getEnd()).isEmpty()) {
                throw new BusinessException(ResponseEnum.BUSINESS_DUPLICATE_DAILY_TRAIN_TICKET_DATE_TRAIN_CODE_START_END);
            }
            // 对Id、createTime、updateTime 重新赋值
            // 可能还需要重新赋值其它的字段，比如 Passenger.memberId
            dailyTrainTicket.setId(CommonUtil.getSnowflakeNextId());
            dailyTrainTicket.setCreateTime(now);
            dailyTrainTicket.setUpdateTime(now);
            dailyTrainTicketMapper.insert(dailyTrainTicket);
            log.info("插入余票信息：{}", dailyTrainTicket);
        } else { // 修改
            dailyTrainTicket.setUpdateTime(now);
            dailyTrainTicketMapper.updateByPrimaryKeySelective(dailyTrainTicket);
            log.info("修改余票信息：{}", dailyTrainTicket);
        }
    }

    @Override
    public PageVo<DailyTrainTicketQueryVo> queryList(DailyTrainTicketQueryForm form) {
        DailyTrainTicketExample dailyTrainTicketExample = new DailyTrainTicketExample();
        dailyTrainTicketExample.setOrderByClause("update_time desc"); // 最新更新的数据，最先被查出来
        DailyTrainTicketExample.Criteria criteria = dailyTrainTicketExample.createCriteria();
        // 这里自定义一些过滤的条件，比如:
//        // 用户只能查自己 memberId 下的余票信息
//        if (ObjectUtil.isNotNull()) {
//            criteria.andMemberIdEqualTo(memberId);
//        }

        // 启动分页
        PageHelper.startPage(form.getPageNum(), form.getPageSize());

        // 获取 dailyTrainTickets
        List<DailyTrainTicket> dailyTrainTickets = dailyTrainTicketMapper.selectByExample(dailyTrainTicketExample);

        // 获得 pageInfo 对象，并将其 List 的模板类型改为 DailyTrainTicketQueryVo
        // 注意这里必须先获取 pageInfo，再尝试获取 List<DailyTrainTicketQueryVo>，否则无法正确获取 pageNum，pages 等重要属性
        PageInfo<DailyTrainTicket> pageInfo = new PageInfo<>(dailyTrainTickets);
        List<DailyTrainTicketQueryVo> voList = BeanUtil.copyToList(pageInfo.getList(), DailyTrainTicketQueryVo.class);

        // 获取 PageVo 对象
        PageVo<DailyTrainTicketQueryVo> vo = BeanUtil.copyProperties(pageInfo, PageVo.class);
        vo.setList(voList);
        vo.setMsg("查询余票信息列表成功");
        return vo;
    }

    @Override
    public void delete(Long id) {
        dailyTrainTicketMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<DailyTrainTicket> queryByTrainCode(String trainCode) {
        DailyTrainTicketExample dailyTrainTicketExample = new DailyTrainTicketExample();
        dailyTrainTicketExample.createCriteria()
                .andTrainCodeEqualTo(trainCode);
        return dailyTrainTicketMapper.selectByExample(dailyTrainTicketExample);
    }

    @Override
    public List<DailyTrainTicket> queryByDate(Date date) {
        DailyTrainTicketExample dailyTrainTicketExample = new DailyTrainTicketExample();
        dailyTrainTicketExample.createCriteria()
                .andDateEqualTo(date);
        return dailyTrainTicketMapper.selectByExample(dailyTrainTicketExample);
    }

    @Override
    public List<DailyTrainTicket> queryByDateAndTrainCodeAndStartAndEnd(Date date, String trainCode, String start, String end) {
        DailyTrainTicketExample dailyTrainTicketExample = new DailyTrainTicketExample();
        dailyTrainTicketExample.createCriteria()
                .andDateEqualTo(date)
                .andTrainCodeEqualTo(trainCode)
                .andStartEqualTo(start)
                .andEndEqualTo(end);
        return dailyTrainTicketMapper.selectByExample(dailyTrainTicketExample);
    }
}
