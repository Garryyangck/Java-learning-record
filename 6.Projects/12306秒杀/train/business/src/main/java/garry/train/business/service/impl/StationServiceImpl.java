package garry.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import garry.train.business.pojo.Train;
import garry.train.business.service.TrainService;
import garry.train.common.enums.ResponseEnum;
import garry.train.common.exception.BusinessException;
import garry.train.common.util.CommonUtil;
import garry.train.common.vo.PageVo;
import garry.train.business.form.StationQueryForm;
import garry.train.business.form.StationSaveForm;
import garry.train.business.mapper.StationMapper;
import garry.train.business.pojo.Station;
import garry.train.business.pojo.StationExample;
import garry.train.business.service.StationService;
import garry.train.business.vo.StationQueryVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Garry
 * 2024-09-19 20:52
 */
@Slf4j
@Service
public class StationServiceImpl implements StationService {
    @Resource
    private TrainService trainService;

    @Resource
    private StationMapper stationMapper;

    @Override
    public void save(StationSaveForm form) {
        Station station = BeanUtil.copyProperties(form, Station.class);
        DateTime now = DateTime.now();

        if (ObjectUtil.isNull(station.getId())) { // 插入
            // 校验唯一键 name_unique
            if (!queryByName(station.getName()).isEmpty()) {
                throw new BusinessException(ResponseEnum.BUSINESS_DUPLICATE_STATION_NAME);
            }

            // 对Id、createTime、updateTime 重新赋值
            // 可能还需要重新赋值其它的字段，比如 Passenger.memberId
            station.setId(CommonUtil.getSnowflakeNextId());
            station.setCreateTime(now);
            station.setUpdateTime(now);
            stationMapper.insert(station);
            log.info("插入车站：{}", station);
        } else { // 修改
            station.setUpdateTime(now);
            stationMapper.updateByPrimaryKeySelective(station);
            log.info("修改车站：{}", station);
        }
    }

    @Override
    public PageVo<StationQueryVo> queryList(StationQueryForm form) {
        StationExample stationExample = new StationExample();
        stationExample.setOrderByClause("name_pinyin"); // 根据拼音排序
        StationExample.Criteria criteria = stationExample.createCriteria();
        // 这里自定义一些过滤的条件，比如:
//        // 用户只能查自己 memberId 下的车站
//        if (ObjectUtil.isNotNull()) {
//            criteria.andMemberIdEqualTo(memberId);
//        }

        // 启动分页
        PageHelper.startPage(form.getPageNum(), form.getPageSize());

        // 获取 stations
        List<Station> stations = stationMapper.selectByExample(stationExample);

        // 获得 pageInfo 对象，并将其 List 的模板类型改为 StationQueryVo
        // 注意这里必须先获取 pageInfo，再尝试获取 List<StationQueryVo>，否则无法正确获取 pageNum，pages 等重要属性
        PageInfo<Station> pageInfo = new PageInfo<>(stations);
        List<StationQueryVo> voList = BeanUtil.copyToList(pageInfo.getList(), StationQueryVo.class);

        // 获取 PageVo 对象
        PageVo<StationQueryVo> vo = BeanUtil.copyProperties(pageInfo, PageVo.class);
        vo.setList(voList);
        vo.setMsg("查询车站列表成功");
        return vo;
    }

    @Override
    public void delete(Long id) {
        // 先得到需要被删除的 Station
        Station station = stationMapper.selectByPrimaryKey(id);

        // 查询作为始发站和终点站的对应 train
        List<Train> trains = new ArrayList<>();
        trains.addAll(trainService.queryByStart(station.getName()));
        trains.addAll(trainService.queryByEnd(station.getName()));

        // 删除这些 train
        for (Train train : trains) {
            trainService.delete(train.getId());
        }

        stationMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<StationQueryVo> queryAll() {
        StationExample stationExample = new StationExample();
        stationExample.setOrderByClause("name_pinyin"); // 根据拼音排序
        StationExample.Criteria criteria = stationExample.createCriteria();
        List<Station> stations = stationMapper.selectByExample(stationExample);
        return BeanUtil.copyToList(stations, StationQueryVo.class);
    }

    @Override
    public List<Station> queryByName(String name) {
        StationExample stationExample = new StationExample();
        stationExample.createCriteria().andNameEqualTo(name);
        return stationMapper.selectByExample(stationExample);
    }
}
