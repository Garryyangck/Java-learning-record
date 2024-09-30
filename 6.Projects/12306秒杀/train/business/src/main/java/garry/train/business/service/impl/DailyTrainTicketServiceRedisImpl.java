package garry.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import garry.train.business.enums.SeatTypeEnum;
import garry.train.business.enums.TrainTypeEnum;
import garry.train.business.form.DailyTrainTicketQueryForm;
import garry.train.business.form.DailyTrainTicketSaveForm;
import garry.train.business.pojo.*;
import garry.train.business.service.DailyTrainTicketService;
import garry.train.business.service.TrainCarriageService;
import garry.train.business.service.TrainStationService;
import garry.train.business.vo.DailyTrainTicketQueryVo;
import garry.train.common.enums.ResponseEnum;
import garry.train.common.exception.BusinessException;
import garry.train.common.util.CommonUtil;
import garry.train.common.util.PageUtil;
import garry.train.common.util.RedisUtil;
import garry.train.common.vo.PageVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author Garry
 * 2024-09-30 20:31
 */
@Slf4j
@Service
@Primary
public class DailyTrainTicketServiceRedisImpl implements DailyTrainTicketService {
    @Resource
    private TrainStationService trainStationService;

    @Resource
    private TrainCarriageService trainCarriageService;

    @Resource
    private RedisTemplate redisTemplate;

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

            // 将 dailyTrainTicket 序列化为 String，存储到 Redis 中
            redisTemplate.opsForValue().set(RedisUtil.getRedisKey4DailyTicket(
                    dailyTrainTicket.getDate(),
                    dailyTrainTicket.getTrainCode(),
                    dailyTrainTicket.getStart(),
                    dailyTrainTicket.getEnd()), JSONObject.toJSONString(dailyTrainTicket));
            log.info("插入余票信息：{}", dailyTrainTicket);
        } else { // 修改
            dailyTrainTicket.setUpdateTime(now);
            redisTemplate.opsForValue().set(RedisUtil.getRedisKey4DailyTicket(
                    dailyTrainTicket.getDate(),
                    dailyTrainTicket.getTrainCode(),
                    dailyTrainTicket.getStart(),
                    dailyTrainTicket.getEnd()), JSONObject.toJSONString(dailyTrainTicket));
            log.info("修改余票信息：{}", dailyTrainTicket);
        }
    }

    @Override
    public PageVo<DailyTrainTicketQueryVo> queryList(DailyTrainTicketQueryForm form) {
        DailyTrainTicketExample dailyTrainTicketExample = new DailyTrainTicketExample();
        dailyTrainTicketExample.setOrderByClause("date desc, train_code, start_index, end_index");

        // 获取所需的所有 RedisKeys
        Set keys = redisTemplate.keys(RedisUtil.getRedisKey4DailyTicket(form.getDate(), form.getCode(), form.getStart(), form.getEnd()));

        // 获取 dailyTrainTickets
        List<DailyTrainTicket> dailyTrainTickets = new ArrayList<>();
        for (Object key : keys) {
            String redisKey = (String) key;
            String dailyTrainTicketJSONString = (String) redisTemplate.opsForValue().get(redisKey);
            DailyTrainTicket dailyTrainTicket = JSON.parseObject(dailyTrainTicketJSONString, DailyTrainTicket.class);
            dailyTrainTickets.add(dailyTrainTicket);
        }

        // 按照 "date desc, train_code, start_index, end_index" 排序
        Collections.sort(dailyTrainTickets, Comparator
                .comparing(DailyTrainTicket::getDate)
                .thenComparing(DailyTrainTicket::getTrainCode)
                .thenComparingInt(DailyTrainTicket::getStartIndex)
                .thenComparingInt(DailyTrainTicket::getEndIndex));

        // 创建 PageInfo 对象
        PageInfo<DailyTrainTicket> pageInfo = PageUtil.getPageInfo(dailyTrainTickets, form.getPageNum(), form.getPageSize());

        // 创建 List<DailyTrainTicketQueryVo>
        List<DailyTrainTicketQueryVo> voList = BeanUtil.copyToList(pageInfo.getList(), DailyTrainTicketQueryVo.class);

        // 获取 PageVo 对象
        PageVo<DailyTrainTicketQueryVo> vo = BeanUtil.copyProperties(pageInfo, PageVo.class);
        vo.setList(voList);
        vo.setMsg("查询余票信息列表成功");
        return vo;
    }

    @Override
    public void delete(Date date, String trainCode, String start, String end) {
        redisTemplate.delete(RedisUtil.getRedisKey4DailyTicket(date, trainCode, start, end));
    }

    @Override
    @Transactional
    public void genDaily(Date date, Train train) {
        long s = System.nanoTime();
        String trainCode = train.getCode();
        // 删除 date, trainCode 下已存在的所有 ticket
        Set keys = redisTemplate.keys(RedisUtil.getRedisKey4DailyTicket(date, trainCode, null, null));
        for (Object key : keys) {
            String redisKey = (String) key;
            redisTemplate.delete(redisKey);
        }

        // 查询所有座位类型的余票数量，int[4]
        int[] seats4SeatTypes = getSeats4SeatTypes(trainCode);

        // 查询所有途经车站
        List<TrainStation> trainStations = trainStationService.queryByTrainCode(trainCode);

        // 获取列车的类型及其价格系数
        BigDecimal priceRate = BigDecimal.ZERO;
        for (TrainTypeEnum trainTypeEnum : TrainTypeEnum.values()) {
            if (trainTypeEnum.getCode().equals(train.getType())) {
                priceRate = trainTypeEnum.getPriceRate();
                break;
            }
        }

        for (int len = 2; len <= trainStations.size(); len++) {
            int startIndex = 0, endIndex = startIndex + len - 1;
            // 计算路程
            BigDecimal distance = new BigDecimal("0");
            for (int i = startIndex + 1; i <= endIndex; i++) {
                distance = distance.add(trainStations.get(i).getKm());
            }
            for (startIndex = 0; (endIndex = startIndex + len - 1) < trainStations.size(); startIndex++) {
                TrainStation startStation = trainStations.get(startIndex);
                TrainStation endStation = trainStations.get(endIndex);

                DailyTrainTicketSaveForm form = new DailyTrainTicketSaveForm();
                form.setDate(date);
                form.setTrainCode(trainCode);

                form.setStart(startStation.getName());
                form.setStartPinyin(startStation.getNamePinyin());
                form.setStartTime(startStation.getOutTime());
                form.setStartIndex(startStation.getIndex());

                form.setEnd(endStation.getName());
                form.setEndPinyin(endStation.getNamePinyin());
                form.setEndTime(endStation.getInTime());
                form.setEndIndex(endStation.getIndex());

                form.setYdz(seats4SeatTypes[0]);
                form.setEdz(seats4SeatTypes[1]);
                form.setRw(seats4SeatTypes[2]);
                form.setYw(seats4SeatTypes[3]);

                // 票价，和 车次类型，座位类型有关
                form.setYdzPrice(distance.multiply(SeatTypeEnum.YDZ.getPrice()).multiply(priceRate));
                form.setEdzPrice(distance.multiply(SeatTypeEnum.EDZ.getPrice()).multiply(priceRate));
                form.setRwPrice(distance.multiply(SeatTypeEnum.RW.getPrice()).multiply(priceRate));
                form.setYwPrice(distance.multiply(SeatTypeEnum.YW.getPrice()).multiply(priceRate));

                save(form);

                if (endIndex + 1 < trainStations.size()) {
                    distance = distance.subtract(trainStations.get(startIndex + 1).getKm());
                    distance = distance.add(trainStations.get(endIndex + 1).getKm());
                }
            }
        }
        log.info("已生成 【{}】 车次 【{}】 的所有每日余票", DateUtil.format(date, "yyyy-MM-dd"), train.getCode());
        log.info("-------------------------- 用时 {} 纳秒 --------------------------", System.nanoTime() - s);
    }

    /**
     * 查询所有座位类型的余票数量
     * 没有的座位类型返回 -1，用于前端特殊展示为 “-”
     */
    private int[] getSeats4SeatTypes(String trainCode) {
        List<TrainCarriage> trainCarriages = trainCarriageService.queryByTrainCode(trainCode);
        int[] seats4SeatTypes = new int[4];
        for (TrainCarriage trainCarriage : trainCarriages) {
            int index = Integer.valueOf(trainCarriage.getSeatType()) - 1;
            seats4SeatTypes[index] += trainCarriage.getSeatCount();
        }
        for (int i = 0; i < 4; i++) {
            seats4SeatTypes[i] = seats4SeatTypes[i] <= 0 ? -1 : seats4SeatTypes[i];
        }
        return seats4SeatTypes;
    }

    @Override
    public List<DailyTrainTicket> queryByDateAndTrainCodeAndStartAndEnd(Date date, String trainCode, String start, String end) {
        DailyTrainTicket dailyTrainTicket = JSON.parseObject((String) redisTemplate.opsForValue().get(RedisUtil.getRedisKey4DailyTicket(date, trainCode, start, end)), DailyTrainTicket.class);
        ArrayList<DailyTrainTicket> list = new ArrayList<>();
        if (ObjectUtil.isNotNull(dailyTrainTicket)) // 要判空，否则会把 null 加进去，也算一个元素，导致抛出 Duplicate 的异常
            list.add(dailyTrainTicket);
        return list;
    }
}
