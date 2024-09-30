package garry.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import garry.train.business.form.TrainStationQueryForm;
import garry.train.business.form.TrainStationSaveForm;
import garry.train.business.mapper.TrainStationMapper;
import garry.train.business.pojo.Train;
import garry.train.business.pojo.TrainStation;
import garry.train.business.pojo.TrainStationExample;
import garry.train.business.service.TrainService;
import garry.train.business.service.TrainStationService;
import garry.train.business.vo.TrainStationQueryVo;
import garry.train.common.enums.ResponseEnum;
import garry.train.common.exception.BusinessException;
import garry.train.common.util.CommonUtil;
import garry.train.common.vo.PageVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Garry
 * 2024-09-22 14:22
 */
@SuppressWarnings("DuplicatedCode")
@Slf4j
@Service
public class TrainStationServiceImpl implements TrainStationService {
    @Resource
    private TrainService trainService;

    @Resource
    private TrainStationMapper trainStationMapper;

    @Override
    public void save(TrainStationSaveForm form) {
        TrainStation trainStation = BeanUtil.copyProperties(form, TrainStation.class);
        DateTime now = DateTime.now();

        if (ObjectUtil.isNull(trainStation.getId())) { // 插入
            // 进行唯一键 train_code_index_unique, train_code_name_unique 校验
            if (!queryByTrainCodeAndIndex(trainStation.getTrainCode(), trainStation.getIndex()).isEmpty()) {
                // 假如插入 index = 2 重复，则把原 index = 2 及后面所有的 index 往后移一位
                TrainStationExample trainStationExample = new TrainStationExample();
                trainStationExample.createCriteria()
                        .andTrainCodeEqualTo(trainStation.getTrainCode())
                        .andIndexGreaterThanOrEqualTo(trainStation.getIndex());
                trainStationExample.setOrderByClause("train_code asc, `index` asc");
                List<TrainStation> trainStations = trainStationMapper.selectByExample(trainStationExample);
                for (int i = trainStations.size() - 1; i >= 0; i--) {
                    TrainStation _trainStation = trainStations.get(i); // 要倒着来改，不然比如 3->4，结果库里还有 4，就插不进去
                    _trainStation.setIndex(_trainStation.getIndex() + 1);
                    _trainStation.setUpdateTime(now);
                    trainStationMapper.updateByPrimaryKeySelective(_trainStation);
                }
            }
            if (!queryByTrainCodeAndName(trainStation.getTrainCode(), trainStation.getName()).isEmpty()) {
                throw new BusinessException(ResponseEnum.BUSINESS_DUPLICATE_TRAIN_STATION_NAME);
            }

            // 对Id、createTime、updateTime 重新赋值
            // 可能还需要重新赋值其它的字段，比如 Passenger.memberId
            trainStation.setId(CommonUtil.getSnowflakeNextId());
            trainStation.setCreateTime(now);
            trainStation.setUpdateTime(now);
            trainStationMapper.insert(trainStation);
            log.info("插入火车车站：{}", trainStation);
        } else { // 修改
            trainStation.setUpdateTime(now);
            trainStationMapper.updateByPrimaryKeySelective(trainStation);
            log.info("修改火车车站：{}", trainStation);
        }
    }

    @Override
    public PageVo<TrainStationQueryVo> queryList(TrainStationQueryForm form) {
        TrainStationExample trainStationExample = new TrainStationExample();
        trainStationExample.setOrderByClause("train_code asc, `index` asc");
        TrainStationExample.Criteria criteria = trainStationExample.createCriteria();
        // 注意用 isNotEmpty 而不是 isNotNUll，按照车次编号过滤
        if (ObjectUtil.isNotEmpty(form.getTrainCode())) {
            criteria.andTrainCodeEqualTo(form.getTrainCode());
        }

        // 启动分页
        PageHelper.startPage(form.getPageNum(), form.getPageSize());

        // 获取 trainStations
        List<TrainStation> trainStations = trainStationMapper.selectByExample(trainStationExample);

        // 获得 pageInfo 对象，并将其 List 的模板类型改为 TrainStationQueryVo
        // 注意这里必须先获取 pageInfo，再尝试获取 List<TrainStationQueryVo>，否则无法正确获取 pageNum，pages 等重要属性
        PageInfo<TrainStation> pageInfo = new PageInfo<>(trainStations);
        List<TrainStationQueryVo> voList = BeanUtil.copyToList(pageInfo.getList(), TrainStationQueryVo.class);

        // 获取 PageVo 对象
        PageVo<TrainStationQueryVo> vo = BeanUtil.copyProperties(pageInfo, PageVo.class);
        vo.setList(voList);
        vo.setMsg("查询火车车站列表成功");
        return vo;
    }

    @Override
    public void delete(Long id) {
        trainStationMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<TrainStation> queryByTrainCodeAndIndex(String trainCode, Integer index) {
        TrainStationExample trainStationExample = new TrainStationExample();
        trainStationExample.createCriteria()
                .andTrainCodeEqualTo(trainCode)
                .andIndexEqualTo(index);
        return trainStationMapper.selectByExample(trainStationExample);
    }

    @Override
    public List<TrainStation> queryByTrainCodeAndName(String trainCode, String name) {
        TrainStationExample trainStationExample = new TrainStationExample();
        trainStationExample.createCriteria()
                .andTrainCodeEqualTo(trainCode)
                .andNameEqualTo(name);
        return trainStationMapper.selectByExample(trainStationExample);
    }

    @Override
    public List<TrainStation> queryByTrainCode(String trainCode) {
        TrainStationExample trainStationExample = new TrainStationExample();
        trainStationExample.createCriteria().andTrainCodeEqualTo(trainCode);
        trainStationExample.setOrderByClause("`index` asc");
        return trainStationMapper.selectByExample(trainStationExample);
    }

    @Override
    public void genTrainStation(String trainCode) {
        // 删除 trainCode 下的所有 station
        deleteByTrainCode(trainCode);

        // 找到起点和终点站
        Train train = trainService.queryByCode(trainCode).get(0);
        if (ObjectUtil.isNull(train)) {
            throw new BusinessException(ResponseEnum.BUSINESS_TRAIN_NOT_FOUND);
        }

        // 取出起点和终点站，以及时间，创建 form
        TrainStationSaveForm startForm = getStartForm(trainCode, train);
        TrainStationSaveForm endForm = getEndForm(trainCode, train);
        save(startForm);
        save(endForm);
    }

    private static TrainStationSaveForm getEndForm(String trainCode, Train train) {
        String end = train.getEnd();
        String endPinyin = train.getEndPinyin();
        Date endTime = train.getEndTime();
        TrainStationSaveForm form = new TrainStationSaveForm();
        form.setTrainCode(trainCode);
        form.setIndex(2);
        form.setName(end);
        form.setNamePinyin(endPinyin);
        form.setInTime(endTime);
        form.setOutTime(endTime);
        form.setStopTime(DateUtil.parse("00:00:00"));
        form.setKm(BigDecimal.valueOf(0));
        return form;
    }

    private static TrainStationSaveForm getStartForm(String trainCode, Train train) {
        String start = train.getStart();
        String startPinyin = train.getStartPinyin();
        Date startTime = train.getStartTime();
        TrainStationSaveForm form = new TrainStationSaveForm();
        form.setTrainCode(trainCode);
        form.setIndex(1);
        form.setName(start);
        form.setNamePinyin(startPinyin);
        form.setInTime(startTime);
        form.setOutTime(startTime);
        form.setStopTime(DateUtil.parse("00:00:00"));
        form.setKm(BigDecimal.valueOf(0));
        return form;
    }

    @Override
    public void deleteByTrainCode(String trainCode) {
        TrainStationExample trainStationExample = new TrainStationExample();
        trainStationExample.createCriteria()
                .andTrainCodeEqualTo(trainCode);
        trainStationMapper.deleteByExample(trainStationExample);
    }
}
