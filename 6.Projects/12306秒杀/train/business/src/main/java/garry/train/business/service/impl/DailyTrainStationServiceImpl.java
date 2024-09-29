package garry.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import garry.train.business.form.DailyTrainStationQueryForm;
import garry.train.business.form.DailyTrainStationSaveForm;
import garry.train.business.mapper.DailyTrainStationMapper;
import garry.train.business.pojo.DailyTrainStation;
import garry.train.business.pojo.DailyTrainStationExample;
import garry.train.business.pojo.Train;
import garry.train.business.pojo.TrainStation;
import garry.train.business.service.DailyTrainStationService;
import garry.train.business.service.TrainStationService;
import garry.train.business.vo.DailyTrainStationQueryVo;
import garry.train.common.enums.ResponseEnum;
import garry.train.common.exception.BusinessException;
import garry.train.common.util.CommonUtil;
import garry.train.common.vo.PageVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Garry
 * 2024-09-28 19:32
 */
@Slf4j
@Service
public class DailyTrainStationServiceImpl implements DailyTrainStationService {
    @Resource
    private TrainStationService trainStationService;

    @Resource
    private DailyTrainStationMapper dailyTrainStationMapper;

    @Override
    public void save(DailyTrainStationSaveForm form) {
        DailyTrainStation dailyTrainStation = BeanUtil.copyProperties(form, DailyTrainStation.class);
        DateTime now = DateTime.now();

        if (ObjectUtil.isNull(dailyTrainStation.getId())) { // 插入
            // 插入时要看数据库有没有唯一键约束，在此校验唯一键约束，防止出现 DuplicationKeyException
            if (!queryByDateAndTrainCodeAndIndex(dailyTrainStation.getDate(), dailyTrainStation.getTrainCode(), dailyTrainStation.getIndex()).isEmpty()) {
                throw new BusinessException(ResponseEnum.BUSINESS_DUPLICATE_DAILY_TRAIN_STATION_DATE_TRAIN_CODE_INDEX);
            }
            if (!queryByDateAndTrainCodeAndName(dailyTrainStation.getDate(), dailyTrainStation.getTrainCode(), dailyTrainStation.getName()).isEmpty()) {
                throw new BusinessException(ResponseEnum.BUSINESS_DUPLICATE_DAILY_TRAIN_STATION_DATE_TRAIN_CODE_NAME);
            }
            // 对Id、createTime、updateTime 重新赋值
            // 可能还需要重新赋值其它的字段，比如 Passenger.memberId
            dailyTrainStation.setId(CommonUtil.getSnowflakeNextId());
            dailyTrainStation.setCreateTime(now);
            dailyTrainStation.setUpdateTime(now);
            dailyTrainStationMapper.insert(dailyTrainStation);
            log.info("插入每日车站：{}", dailyTrainStation);
        } else { // 修改
            dailyTrainStation.setUpdateTime(now);
            dailyTrainStationMapper.updateByPrimaryKeySelective(dailyTrainStation);
            log.info("修改每日车站：{}", dailyTrainStation);
        }
    }

    @Override
    public PageVo<DailyTrainStationQueryVo> queryList(DailyTrainStationQueryForm form) {
        DailyTrainStationExample dailyTrainStationExample = new DailyTrainStationExample();
        dailyTrainStationExample.setOrderByClause("date desc, train_code asc, `index` asc");
        DailyTrainStationExample.Criteria criteria = dailyTrainStationExample.createCriteria();
        // 注意用 isNotEmpty 而不是 isNotNUll，按照车次编号过滤
        if (ObjectUtil.isNotEmpty(form.getTrainCode())) {
            criteria.andTrainCodeEqualTo(form.getTrainCode());
        }
        if (ObjectUtil.isNotEmpty(form.getDate())) {
            criteria.andDateEqualTo(form.getDate());
        }

        // 启动分页
        PageHelper.startPage(form.getPageNum(), form.getPageSize());

        // 获取 dailyTrainStations
        List<DailyTrainStation> dailyTrainStations = dailyTrainStationMapper.selectByExample(dailyTrainStationExample);

        // 获得 pageInfo 对象，并将其 List 的模板类型改为 DailyTrainStationQueryVo
        // 注意这里必须先获取 pageInfo，再尝试获取 List<DailyTrainStationQueryVo>，否则无法正确获取 pageNum，pages 等重要属性
        PageInfo<DailyTrainStation> pageInfo = new PageInfo<>(dailyTrainStations);
        List<DailyTrainStationQueryVo> voList = BeanUtil.copyToList(pageInfo.getList(), DailyTrainStationQueryVo.class);

        // 获取 PageVo 对象
        PageVo<DailyTrainStationQueryVo> vo = BeanUtil.copyProperties(pageInfo, PageVo.class);
        vo.setList(voList);
        vo.setMsg("查询每日车站列表成功");
        return vo;
    }

    @Override
    public void delete(Long id) {
        dailyTrainStationMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void genDaily(Date date, Train train) {
        // 删除 date 下 train 的所有 daily-train-station
        DailyTrainStationExample dailyTrainStationExample = new DailyTrainStationExample();
        dailyTrainStationExample.createCriteria()
                .andDateEqualTo(date)
                .andTrainCodeEqualTo(train.getCode());
        dailyTrainStationMapper.deleteByExample(dailyTrainStationExample);

        // 查出 date 下 train 下所有的 train-station
        List<TrainStation> trainStations = trainStationService.queryByTrainCode(train.getCode());
        for (TrainStation trainStation : trainStations) {
            DailyTrainStation dailyTrainStation = BeanUtil.copyProperties(trainStation, DailyTrainStation.class);
            dailyTrainStation.setDate(date);
            dailyTrainStation.setId(null); // 防止跑到修改去了
            dailyTrainStation.setCreateTime(null);
            dailyTrainStation.setUpdateTime(null);
            save(BeanUtil.copyProperties(dailyTrainStation, DailyTrainStationSaveForm.class));
        }
    }

    @Override
    public List<DailyTrainStation> queryByDateAndTrainCodeAndIndex(Date date, String trainCode, Integer index) {
        DailyTrainStationExample dailyTrainStationExample = new DailyTrainStationExample();
        dailyTrainStationExample.createCriteria()
                .andDateEqualTo(date)
                .andTrainCodeEqualTo(trainCode)
                .andIndexEqualTo(index);
        return dailyTrainStationMapper.selectByExample(dailyTrainStationExample);
    }

    @Override
    public List<DailyTrainStation> queryByDateAndTrainCodeAndName(Date date, String trainCode, String name) {
        DailyTrainStationExample dailyTrainStationExample = new DailyTrainStationExample();
        dailyTrainStationExample.createCriteria()
                .andDateEqualTo(date)
                .andTrainCodeEqualTo(trainCode)
                .andNameEqualTo(name);
        return dailyTrainStationMapper.selectByExample(dailyTrainStationExample);
    }
}
