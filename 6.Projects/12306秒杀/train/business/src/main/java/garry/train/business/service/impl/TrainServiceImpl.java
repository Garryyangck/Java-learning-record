package garry.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import garry.train.business.enums.TrainTypeEnum;
import garry.train.business.form.TrainQueryForm;
import garry.train.business.form.TrainSaveForm;
import garry.train.business.mapper.TrainMapper;
import garry.train.business.pojo.Train;
import garry.train.business.pojo.TrainCarriage;
import garry.train.business.pojo.TrainExample;
import garry.train.business.pojo.TrainStation;
import garry.train.business.service.TrainCarriageService;
import garry.train.business.service.TrainService;
import garry.train.business.service.TrainStationService;
import garry.train.business.vo.TrainQueryAllVo;
import garry.train.business.vo.TrainQueryVo;
import garry.train.common.util.CommonUtil;
import garry.train.common.vo.PageVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @author Garry
 * 2024-09-22 13:19
 */
@Slf4j
@Service
public class TrainServiceImpl implements TrainService {
    @Resource
    private TrainStationService trainStationService;

    @Resource
    private TrainCarriageService trainCarriageService;

    @Resource
    private TrainMapper trainMapper;

    @Override
    public void save(TrainSaveForm form) {
        Train train = BeanUtil.copyProperties(form, Train.class);
        DateTime now = DateTime.now();

        if (ObjectUtil.isNull(train.getId())) { // 插入
            // 对Id、createTime、updateTime 重新赋值
            train.setId(CommonUtil.getSnowflakeNextId());
            train.setCreateTime(now);
            train.setUpdateTime(now);

            // 为 code 赋值，格式为“type.code+数据库中的数目+三位随机数”
            StringBuilder code = new StringBuilder();
            do {
                code.append(train.getType());
                long count = trainMapper.countByExample(new TrainExample());
                StringBuilder cnt = new StringBuilder();
                if(count < 10)
                    cnt.append("00");
                else if(count < 100)
                    cnt.append("0");
                cnt.append(count);
                code.append(cnt);
                String snowflake = String.valueOf(CommonUtil.getSnowflakeNextId());
                code.append(snowflake.substring(snowflake.length() - 3));
            } while (!queryByCode(code.toString()).isEmpty()); // 校验 code 是否唯一

            train.setCode(code.toString());
            trainMapper.insert(train);
            log.info("插入车次：{}", train);
        } else { // 修改
            train.setUpdateTime(now);
            trainMapper.updateByPrimaryKeySelective(train);
            log.info("修改车次：{}", train);
        }

        // 更新缓存
        queryAllRefreshCache();
    }

    @Override
    public PageVo<TrainQueryVo> queryList(TrainQueryForm form) {
        TrainExample trainExample = new TrainExample();
        trainExample.setOrderByClause("code");
        TrainExample.Criteria criteria = trainExample.createCriteria();
        // 注意用 isNotEmpty 而不是 isNotNUll，按照车次编号过滤
        if (ObjectUtil.isNotEmpty(form.getCode())) {
            criteria.andCodeEqualTo(form.getCode());
        }

        // 启动分页
        PageHelper.startPage(form.getPageNum(), form.getPageSize());

        // 获取 trains
        List<Train> trains = trainMapper.selectByExample(trainExample);

        // 获得 pageInfo 对象，并将其 List 的模板类型改为 TrainQueryVo
        // 注意这里必须先获取 pageInfo，再尝试获取 List<TrainQueryVo>，否则无法正确获取 pageNum，pages 等重要属性
        PageInfo<Train> pageInfo = new PageInfo<>(trains);
        List<TrainQueryVo> voList = BeanUtil.copyToList(pageInfo.getList(), TrainQueryVo.class);

        // 获取 PageVo 对象
        PageVo<TrainQueryVo> vo = BeanUtil.copyProperties(pageInfo, PageVo.class);
        vo.setList(voList);
        vo.setMsg("查询车次列表成功");
        return vo;
    }

    @Override
    public void delete(Long id) {
        // 查出该 Train
        Train train = trainMapper.selectByPrimaryKey(id);

        // 查出 trainCode 下的 trainStation 和 trainCarriage
        List<TrainStation> trainStations = trainStationService.queryByTrainCode(train.getCode());
        List<TrainCarriage> trainCarriages = trainCarriageService.queryByTrainCode(train.getCode());

        // 删除它们
        for (TrainStation trainStation : trainStations) {
            trainStationService.delete(trainStation.getId());
        }
        for (TrainCarriage trainCarriage : trainCarriages) {
            trainCarriageService.delete(trainCarriage.getId());
        }

        trainMapper.deleteByPrimaryKey(id);

        // 更新缓存
        queryAllRefreshCache();
    }

    @CachePut(value = "TrainServiceImpl.queryAll")
    public List<TrainQueryAllVo> queryAllRefreshCache() {
        return queryAll();
    }

    @Override
    @Cacheable(value = "TrainServiceImpl.queryAll")
    public List<TrainQueryAllVo> queryAll() {
        TrainExample trainExample = new TrainExample();
        trainExample.setOrderByClause("code");
        List<Train> trains = trainMapper.selectByExample(trainExample).stream()
                // 将 type 由 "G" 换为 "高铁"
                .peek(train -> {
                    List<HashMap<String, String>> enumList = TrainTypeEnum.getEnumList();
                    for (HashMap<String, String> anEnum : enumList)
                        if (anEnum.get("code").equals(train.getType()))
                            train.setType(anEnum.get("desc"));
                }).toList();
        return BeanUtil.copyToList(trains, TrainQueryAllVo.class);
    }

    @Override
    public List<Train> selectAll() {
        TrainExample trainExample = new TrainExample();
        trainExample.setOrderByClause("code");
        return trainMapper.selectByExample(trainExample);
    }

    @Override
    public List<Train> queryByCode(String code) {
        TrainExample trainExample = new TrainExample();
        trainExample.createCriteria().andCodeEqualTo(code);
        return trainMapper.selectByExample(trainExample);
    }

    @Override
    public List<Train> queryByStart(String start) {
        TrainExample trainExample = new TrainExample();
        trainExample.createCriteria().andStartEqualTo(start);
        return trainMapper.selectByExample(trainExample);
    }

    @Override
    public List<Train> queryByEnd(String end) {
        TrainExample trainExample = new TrainExample();
        trainExample.createCriteria().andEndEqualTo(end);
        return trainMapper.selectByExample(trainExample);
    }
}
