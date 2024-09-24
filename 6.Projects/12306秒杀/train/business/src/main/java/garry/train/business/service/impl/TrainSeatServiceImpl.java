package garry.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import garry.train.common.util.CommonUtil;
import garry.train.common.vo.PageVo;
import garry.train.business.form.TrainSeatQueryForm;
import garry.train.business.form.TrainSeatSaveForm;
import garry.train.business.mapper.TrainSeatMapper;
import garry.train.business.pojo.TrainSeat;
import garry.train.business.pojo.TrainSeatExample;
import garry.train.business.service.TrainSeatService;
import garry.train.business.vo.TrainSeatQueryVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Garry
 * 2024-09-22 21:13
 */
@Slf4j
@Service
public class TrainSeatServiceImpl implements TrainSeatService {
    @Resource
    private TrainSeatMapper trainSeatMapper;

    @Override
    public void save(TrainSeatSaveForm form) {
        TrainSeat trainSeat = BeanUtil.copyProperties(form, TrainSeat.class);
        DateTime now = DateTime.now();

        if (ObjectUtil.isNull(trainSeat.getId())) { // 插入
            // 对Id、createTime、updateTime 重新赋值
            // 可能还需要重新赋值其它的字段，比如 Passenger.memberId
            trainSeat.setId(CommonUtil.getSnowflakeNextId());
            trainSeat.setCreateTime(now);
            trainSeat.setUpdateTime(now);
            trainSeatMapper.insert(trainSeat);
            log.info("插入座位：{}", trainSeat);
        } else { // 修改
            trainSeat.setUpdateTime(now);
            trainSeatMapper.updateByPrimaryKeySelective(trainSeat);
            log.info("修改座位：{}", trainSeat);
        }
    }

    @Override
    public PageVo<TrainSeatQueryVo> queryList(TrainSeatQueryForm form) {
        TrainSeatExample trainSeatExample = new TrainSeatExample();
        trainSeatExample.setOrderByClause("train_code asc, carriage_index asc, row asc"); // 最新更新的数据，最先被查出来
        TrainSeatExample.Criteria criteria = trainSeatExample.createCriteria();
        // 注意用 isNotEmpty 而不是 isNotNUll，按照车次编号过滤
        if (ObjectUtil.isNotEmpty(form.getTrainCode())) {
            criteria.andTrainCodeEqualTo(form.getTrainCode());
        }

        // 启动分页
        PageHelper.startPage(form.getPageNum(), form.getPageSize());

        // 获取 trainSeats
        List<TrainSeat> trainSeats = trainSeatMapper.selectByExample(trainSeatExample);

        // 获得 pageInfo 对象，并将其 List 的模板类型改为 TrainSeatQueryVo
        // 注意这里必须先获取 pageInfo，再尝试获取 List<TrainSeatQueryVo>，否则无法正确获取 pageNum，pages 等重要属性
        PageInfo<TrainSeat> pageInfo = new PageInfo<>(trainSeats);
        List<TrainSeatQueryVo> voList = BeanUtil.copyToList(pageInfo.getList(), TrainSeatQueryVo.class);

        // 获取 PageVo 对象
        PageVo<TrainSeatQueryVo> vo = BeanUtil.copyProperties(pageInfo, PageVo.class);
        vo.setList(voList);
        vo.setMsg("查询座位列表成功");
        return vo;
    }

    @Override
    public void delete(Long id) {
        trainSeatMapper.deleteByPrimaryKey(id);
    }
}
