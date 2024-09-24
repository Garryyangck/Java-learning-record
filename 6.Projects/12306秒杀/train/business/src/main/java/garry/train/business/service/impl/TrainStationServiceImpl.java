package garry.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import garry.train.common.util.CommonUtil;
import garry.train.common.vo.PageVo;
import garry.train.business.form.TrainStationQueryForm;
import garry.train.business.form.TrainStationSaveForm;
import garry.train.business.mapper.TrainStationMapper;
import garry.train.business.pojo.TrainStation;
import garry.train.business.pojo.TrainStationExample;
import garry.train.business.service.TrainStationService;
import garry.train.business.vo.TrainStationQueryVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Garry
 * 2024-09-22 14:22
 */
@Slf4j
@Service
public class TrainStationServiceImpl implements TrainStationService {
    @Resource
    private TrainStationMapper trainStationMapper;

    @Override
    public void save(TrainStationSaveForm form) {
        TrainStation trainStation = BeanUtil.copyProperties(form, TrainStation.class);
        DateTime now = DateTime.now();

        if (ObjectUtil.isNull(trainStation.getId())) { // 插入
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
}
