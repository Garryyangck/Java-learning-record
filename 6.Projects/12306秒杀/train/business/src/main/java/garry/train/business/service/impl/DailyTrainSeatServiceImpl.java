package garry.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import garry.train.business.form.DailyTrainSeatQueryForm;
import garry.train.business.form.DailyTrainSeatSaveForm;
import garry.train.business.mapper.DailyTrainSeatMapper;
import garry.train.business.pojo.DailyTrainSeat;
import garry.train.business.pojo.DailyTrainSeatExample;
import garry.train.business.pojo.Train;
import garry.train.business.pojo.TrainSeat;
import garry.train.business.service.DailyTrainSeatService;
import garry.train.business.service.TrainSeatService;
import garry.train.business.service.TrainService;
import garry.train.business.service.TrainStationService;
import garry.train.business.util.SellUtil;
import garry.train.business.vo.DailyTrainSeatQueryVo;
import garry.train.common.util.CommonUtil;
import garry.train.common.vo.PageVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author Garry
 * 2024-10-09 00:00
 */
@Slf4j
@Service
public class DailyTrainSeatServiceImpl implements DailyTrainSeatService {
    @Resource
    private TrainService trainService;

    @Resource
    private TrainSeatService trainSeatService;

    @Resource
    private TrainStationService trainStationService;

    @Resource
    private DailyTrainSeatMapper dailyTrainSeatMapper;

    @Override
    public void save(DailyTrainSeatSaveForm form) {
        DailyTrainSeat dailyTrainSeat = BeanUtil.copyProperties(form, DailyTrainSeat.class);
        DateTime now = DateTime.now();

        if (ObjectUtil.isNull(dailyTrainSeat.getId())) { // 插入
            // 插入时要看数据库有没有唯一键约束，在此校验唯一键约束，防止出现 DuplicationKeyException

            // 对Id、createTime、updateTime 重新赋值
            // 可能还需要重新赋值其它的字段，比如 Passenger.memberId
            dailyTrainSeat.setId(CommonUtil.getSnowflakeNextId());
            dailyTrainSeat.setCreateTime(now);
            dailyTrainSeat.setUpdateTime(now);
            dailyTrainSeatMapper.insert(dailyTrainSeat);
            log.info("插入每日座位：{}", dailyTrainSeat);
        } else { // 修改
            dailyTrainSeat.setUpdateTime(now);
            dailyTrainSeatMapper.updateByPrimaryKeySelective(dailyTrainSeat);
            log.info("修改每日座位：{}", dailyTrainSeat);
        }
    }

    @Override
    public PageVo<DailyTrainSeatQueryVo> queryList(DailyTrainSeatQueryForm form) {
        DailyTrainSeatExample dailyTrainSeatExample = new DailyTrainSeatExample();
        dailyTrainSeatExample.setOrderByClause("date desc, train_code asc, carriage_index asc, row asc, carriage_seat_index asc");
        DailyTrainSeatExample.Criteria criteria = dailyTrainSeatExample.createCriteria();
        // 注意用 isNotEmpty 而不是 isNotNUll，按照车次编号过滤
        if (ObjectUtil.isNotEmpty(form.getTrainCode())) {
            criteria.andTrainCodeEqualTo(form.getTrainCode());
        }
        if (ObjectUtil.isNotEmpty(form.getDate())) {
            criteria.andDateEqualTo(form.getDate());
        }

        // 启动分页
        PageHelper.startPage(form.getPageNum(), form.getPageSize());

        // 获取 dailyTrainSeats
        List<DailyTrainSeat> dailyTrainSeats = dailyTrainSeatMapper.selectByExample(dailyTrainSeatExample);

        // 获得 pageInfo 对象，并将其 List 的模板类型改为 DailyTrainSeatQueryVo
        // 注意这里必须先获取 pageInfo，再尝试获取 List<DailyTrainSeatQueryVo>，否则无法正确获取 pageNum，pages 等重要属性
        PageInfo<DailyTrainSeat> pageInfo = new PageInfo<>(dailyTrainSeats);
        List<DailyTrainSeatQueryVo> voList = BeanUtil.copyToList(pageInfo.getList(), DailyTrainSeatQueryVo.class);

        // 将 DailyTrainSeatQueryVo.sell(String) 从 "0"(Integer) 改为 BinaryString
        // 注意，BinaryString 的长度应该为车次车站数 - 1，如果不够，需要向左补 '0'
        // 为了解决 trainCode 为空，全查出来时，各个不同车次 sell 长度不同的问题，可以先查一个 map，然后 stream 中通过 map 查对应的 len
        HashMap<String/*trainCode*/, Integer/*binaryStringLen*/> map = new HashMap<>(); // <trainCode, binaryStringLen>
        List<Train> trains = trainService.selectAll();
        for (Train train : trains) {
            int binaryStringLen = trainStationService.queryByTrainCode(train.getCode()).size() - 1;
            map.put(train.getCode(), binaryStringLen);
        }
        voList = voList.stream()
                .peek(vo -> vo.setSell(SellUtil.getBinaryString(
                        Integer.parseInt(vo.getSell()),
                        map.get(vo.getTrainCode())))
                ).toList();

        // 获取 PageVo 对象
        PageVo<DailyTrainSeatQueryVo> vo = BeanUtil.copyProperties(pageInfo, PageVo.class);
        vo.setList(voList);
        vo.setMsg("查询每日座位列表成功");
        return vo;
    }

    @Override
    public void delete(Long id) {
        dailyTrainSeatMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional
    public void genDaily(Date date, Train train) {
        // 删除 date 下 train 的所有 daily-train-seat
        DailyTrainSeatExample dailyTrainSeatExample = new DailyTrainSeatExample();
        dailyTrainSeatExample.createCriteria()
                .andDateEqualTo(date)
                .andTrainCodeEqualTo(train.getCode());
        dailyTrainSeatMapper.deleteByExample(dailyTrainSeatExample);

        // 查出 date 下 train 下所有的 train-seat
        List<TrainSeat> trainSeats = trainSeatService.queryByTrainCode(train.getCode());
        for (TrainSeat trainSeat : trainSeats) {
            DailyTrainSeat dailyTrainSeat = BeanUtil.copyProperties(trainSeat, DailyTrainSeat.class);
            dailyTrainSeat.setDate(date);
            dailyTrainSeat.setSell(0); // 初始座位情况肯定是 int(0)
            dailyTrainSeat.setId(null); // 防止跑到修改去了
            dailyTrainSeat.setCreateTime(null);
            dailyTrainSeat.setUpdateTime(null);
            save(BeanUtil.copyProperties(dailyTrainSeat, DailyTrainSeatSaveForm.class));
        }
        log.info("已生成 【{}】 车次 【{}】 的所有每日座位", DateUtil.format(date, "yyyy-MM-dd"), train.getCode());
    }
}
