package garry.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import garry.train.common.util.CommonUtil;
import garry.train.common.vo.PageVo;
import garry.train.business.form.DailyTrainCarriageQueryForm;
import garry.train.business.form.DailyTrainCarriageSaveForm;
import garry.train.business.mapper.DailyTrainCarriageMapper;
import garry.train.business.pojo.DailyTrainCarriage;
import garry.train.business.pojo.DailyTrainCarriageExample;
import garry.train.business.service.DailyTrainCarriageService;
import garry.train.business.vo.DailyTrainCarriageQueryVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Garry
 * 2024-09-28 20:20
 */
@Slf4j
@Service
public class DailyTrainCarriageServiceImpl implements DailyTrainCarriageService {
    @Resource
    private DailyTrainCarriageMapper dailyTrainCarriageMapper;

    @Override
    public void save(DailyTrainCarriageSaveForm form) {
        DailyTrainCarriage dailyTrainCarriage = BeanUtil.copyProperties(form, DailyTrainCarriage.class);
        DateTime now = DateTime.now();

        if (ObjectUtil.isNull(dailyTrainCarriage.getId())) { // 插入
            // 插入时要看数据库有没有唯一键约束，在此校验唯一键约束，防止出现 DuplicationKeyException

            // 对Id、createTime、updateTime 重新赋值
            // 可能还需要重新赋值其它的字段，比如 Passenger.memberId
            dailyTrainCarriage.setId(CommonUtil.getSnowflakeNextId());
            dailyTrainCarriage.setCreateTime(now);
            dailyTrainCarriage.setUpdateTime(now);
            dailyTrainCarriageMapper.insert(dailyTrainCarriage);
            log.info("插入每日车厢：{}", dailyTrainCarriage);
        } else { // 修改
            dailyTrainCarriage.setUpdateTime(now);
            dailyTrainCarriageMapper.updateByPrimaryKeySelective(dailyTrainCarriage);
            log.info("修改每日车厢：{}", dailyTrainCarriage);
        }
    }

    @Override
    public PageVo<DailyTrainCarriageQueryVo> queryList(DailyTrainCarriageQueryForm form) {
        DailyTrainCarriageExample dailyTrainCarriageExample = new DailyTrainCarriageExample();
        dailyTrainCarriageExample.setOrderByClause("date desc, train_code asc, `index` asc"); // 最新更新的数据，最先被查出来
        DailyTrainCarriageExample.Criteria criteria = dailyTrainCarriageExample.createCriteria();
        // 注意用 isNotEmpty 而不是 isNotNUll，按照车次编号过滤
        if (ObjectUtil.isNotEmpty(form.getTrainCode())) {
            criteria.andTrainCodeEqualTo(form.getTrainCode());
        }
        if (ObjectUtil.isNotEmpty(form.getDate())) {
            criteria.andDateEqualTo(form.getDate());
        }

        // 启动分页
        PageHelper.startPage(form.getPageNum(), form.getPageSize());

        // 获取 dailyTrainCarriages
        List<DailyTrainCarriage> dailyTrainCarriages = dailyTrainCarriageMapper.selectByExample(dailyTrainCarriageExample);

        // 获得 pageInfo 对象，并将其 List 的模板类型改为 DailyTrainCarriageQueryVo
        // 注意这里必须先获取 pageInfo，再尝试获取 List<DailyTrainCarriageQueryVo>，否则无法正确获取 pageNum，pages 等重要属性
        PageInfo<DailyTrainCarriage> pageInfo = new PageInfo<>(dailyTrainCarriages);
        List<DailyTrainCarriageQueryVo> voList = BeanUtil.copyToList(pageInfo.getList(), DailyTrainCarriageQueryVo.class);

        // 获取 PageVo 对象
        PageVo<DailyTrainCarriageQueryVo> vo = BeanUtil.copyProperties(pageInfo, PageVo.class);
        vo.setList(voList);
        vo.setMsg("查询每日车厢列表成功");
        return vo;
    }

    @Override
    public void delete(Long id) {
        dailyTrainCarriageMapper.deleteByPrimaryKey(id);
    }
}
