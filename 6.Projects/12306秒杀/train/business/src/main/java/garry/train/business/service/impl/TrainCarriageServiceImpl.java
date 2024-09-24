package garry.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import garry.train.common.util.CommonUtil;
import garry.train.common.vo.PageVo;
import garry.train.business.form.TrainCarriageQueryForm;
import garry.train.business.form.TrainCarriageSaveForm;
import garry.train.business.mapper.TrainCarriageMapper;
import garry.train.business.pojo.TrainCarriage;
import garry.train.business.pojo.TrainCarriageExample;
import garry.train.business.service.TrainCarriageService;
import garry.train.business.vo.TrainCarriageQueryVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Garry
 * 2024-09-22 20:48
 */
@Slf4j
@Service
public class TrainCarriageServiceImpl implements TrainCarriageService {
    @Resource
    private TrainCarriageMapper trainCarriageMapper;

    @Override
    public void save(TrainCarriageSaveForm form) {
        TrainCarriage trainCarriage = BeanUtil.copyProperties(form, TrainCarriage.class);
        DateTime now = DateTime.now();

        if (ObjectUtil.isNull(trainCarriage.getId())) { // 插入
            // 对Id、createTime、updateTime 重新赋值
            // 可能还需要重新赋值其它的字段，比如 Passenger.memberId
            trainCarriage.setId(CommonUtil.getSnowflakeNextId());
            trainCarriage.setCreateTime(now);
            trainCarriage.setUpdateTime(now);
            trainCarriageMapper.insert(trainCarriage);
            log.info("插入火车车厢：{}", trainCarriage);
        } else { // 修改
            trainCarriage.setUpdateTime(now);
            trainCarriageMapper.updateByPrimaryKeySelective(trainCarriage);
            log.info("修改火车车厢：{}", trainCarriage);
        }
    }

    @Override
    public PageVo<TrainCarriageQueryVo> queryList(TrainCarriageQueryForm form) {
        TrainCarriageExample trainCarriageExample = new TrainCarriageExample();
        trainCarriageExample.setOrderByClause("train_code asc, `index` asc");
        TrainCarriageExample.Criteria criteria = trainCarriageExample.createCriteria();
        // 注意用 isNotEmpty 而不是 isNotNUll，按照车次编号过滤
        if (ObjectUtil.isNotEmpty(form.getTrainCode())) {
            criteria.andTrainCodeEqualTo(form.getTrainCode());
        }

        // 启动分页
        PageHelper.startPage(form.getPageNum(), form.getPageSize());

        // 获取 trainCarriages
        List<TrainCarriage> trainCarriages = trainCarriageMapper.selectByExample(trainCarriageExample);

        // 获得 pageInfo 对象，并将其 List 的模板类型改为 TrainCarriageQueryVo
        // 注意这里必须先获取 pageInfo，再尝试获取 List<TrainCarriageQueryVo>，否则无法正确获取 pageNum，pages 等重要属性
        PageInfo<TrainCarriage> pageInfo = new PageInfo<>(trainCarriages);
        List<TrainCarriageQueryVo> voList = BeanUtil.copyToList(pageInfo.getList(), TrainCarriageQueryVo.class);

        // 获取 PageVo 对象
        PageVo<TrainCarriageQueryVo> vo = BeanUtil.copyProperties(pageInfo, PageVo.class);
        vo.setList(voList);
        vo.setMsg("查询火车车厢列表成功");
        return vo;
    }

    @Override
    public void delete(Long id) {
        trainCarriageMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<TrainCarriage> selectByTrainCode(String trainCode) {
        TrainCarriageExample trainCarriageExample = new TrainCarriageExample();
        trainCarriageExample.createCriteria().andTrainCodeEqualTo(trainCode);
        return trainCarriageMapper.selectByExample(trainCarriageExample);
    }
}
