package garry.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import garry.train.common.util.CommonUtil;
import garry.train.common.vo.PageVo;
import garry.train.business.form.DailyTrainQueryForm;
import garry.train.business.form.DailyTrainSaveForm;
import garry.train.business.mapper.DailyTrainMapper;
import garry.train.business.pojo.DailyTrain;
import garry.train.business.pojo.DailyTrainExample;
import garry.train.business.service.DailyTrainService;
import garry.train.business.vo.DailyTrainQueryVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Garry
 * 2024-09-28 16:33
 */
@Slf4j
@Service
public class DailyTrainServiceImpl implements DailyTrainService {
    @Resource
    private DailyTrainMapper dailyTrainMapper;

    @Override
    public void save(DailyTrainSaveForm form) {
        DailyTrain dailyTrain = BeanUtil.copyProperties(form, DailyTrain.class);
        DateTime now = DateTime.now();

        if (ObjectUtil.isNull(dailyTrain.getId())) { // 插入
            // 插入时要看数据库有没有唯一键约束，在此校验唯一键约束，防止出现 DuplicationKeyException

            // 对Id、createTime、updateTime 重新赋值
            // 可能还需要重新赋值其它的字段，比如 Passenger.memberId
            dailyTrain.setId(CommonUtil.getSnowflakeNextId());
            dailyTrain.setCreateTime(now);
            dailyTrain.setUpdateTime(now);
            dailyTrainMapper.insert(dailyTrain);
            log.info("插入每日车次：{}", dailyTrain);
        } else { // 修改
            dailyTrain.setUpdateTime(now);
            dailyTrainMapper.updateByPrimaryKeySelective(dailyTrain);
            log.info("修改每日车次：{}", dailyTrain);
        }
    }

    @Override
    public PageVo<DailyTrainQueryVo> queryList(DailyTrainQueryForm form) {
        DailyTrainExample dailyTrainExample = new DailyTrainExample();
        dailyTrainExample.setOrderByClause("date desc, code");
        DailyTrainExample.Criteria criteria = dailyTrainExample.createCriteria();
        // 注意用 isNotEmpty 而不是 isNotNUll，按照车次编号过滤
        if (ObjectUtil.isNotEmpty(form.getCode())) {
            criteria.andCodeEqualTo(form.getCode());
        }
        if (ObjectUtil.isNotEmpty(form.getDate())) {
            criteria.andDateEqualTo(form.getDate());
        }

        // 启动分页
        PageHelper.startPage(form.getPageNum(), form.getPageSize());

        // 获取 dailyTrains
        List<DailyTrain> dailyTrains = dailyTrainMapper.selectByExample(dailyTrainExample);

        // 获得 pageInfo 对象，并将其 List 的模板类型改为 DailyTrainQueryVo
        // 注意这里必须先获取 pageInfo，再尝试获取 List<DailyTrainQueryVo>，否则无法正确获取 pageNum，pages 等重要属性
        PageInfo<DailyTrain> pageInfo = new PageInfo<>(dailyTrains);
        List<DailyTrainQueryVo> voList = BeanUtil.copyToList(pageInfo.getList(), DailyTrainQueryVo.class);

        // 获取 PageVo 对象
        PageVo<DailyTrainQueryVo> vo = BeanUtil.copyProperties(pageInfo, PageVo.class);
        vo.setList(voList);
        vo.setMsg("查询每日车次列表成功");
        return vo;
    }

    @Override
    public void delete(Long id) {
        dailyTrainMapper.deleteByPrimaryKey(id);
    }
}
