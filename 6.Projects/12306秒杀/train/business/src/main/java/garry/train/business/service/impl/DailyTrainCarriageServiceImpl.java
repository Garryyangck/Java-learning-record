package garry.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import garry.train.business.form.DailyTrainCarriageQueryForm;
import garry.train.business.form.DailyTrainCarriageSaveForm;
import garry.train.business.mapper.DailyTrainCarriageMapper;
import garry.train.business.pojo.DailyTrainCarriage;
import garry.train.business.pojo.DailyTrainCarriageExample;
import garry.train.business.pojo.Train;
import garry.train.business.pojo.TrainCarriage;
import garry.train.business.service.DailyTrainCarriageService;
import garry.train.business.service.TrainCarriageService;
import garry.train.business.vo.DailyTrainCarriageQueryVo;
import garry.train.common.enums.ResponseEnum;
import garry.train.common.exception.BusinessException;
import garry.train.common.util.CommonUtil;
import garry.train.common.vo.PageVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author Garry
 * 2024-09-28 20:20
 */
@Slf4j
@Service
public class DailyTrainCarriageServiceImpl implements DailyTrainCarriageService {
    @Resource
    private TrainCarriageService trainCarriageService;

    @Resource
    private DailyTrainCarriageMapper dailyTrainCarriageMapper;

    @Override
    public void save(DailyTrainCarriageSaveForm form) {
        DailyTrainCarriage dailyTrainCarriage = BeanUtil.copyProperties(form, DailyTrainCarriage.class);
        DateTime now = DateTime.now();

        if (ObjectUtil.isNull(dailyTrainCarriage.getId())) { // 插入
            // 插入时要看数据库有没有唯一键约束，在此校验唯一键约束，防止出现 DuplicationKeyException
            if(!queryByDateAndTrainCodeAndIndex(dailyTrainCarriage.getDate(),dailyTrainCarriage.getTrainCode(),dailyTrainCarriage.getIndex()).isEmpty()){
                throw new BusinessException(ResponseEnum.BUSINESS_DUPLICATE_DAILY_TRAIN_CARRIAGE_DATE_TRAIN_CODE_INDEX);
            }
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
        dailyTrainCarriageExample.setOrderByClause("date desc, train_code asc, `index` asc");
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

    @Override
    @Transactional
    public void genDaily(Date date, Train train) {
        // 删除 date 下 train 的所有 daily-train-carriage
        DailyTrainCarriageExample dailyTrainCarriageExample = new DailyTrainCarriageExample();
        dailyTrainCarriageExample.createCriteria()
                .andDateEqualTo(date)
                .andTrainCodeEqualTo(train.getCode());
        dailyTrainCarriageMapper.deleteByExample(dailyTrainCarriageExample);

        // 查出 date 下 train 下所有的 train-carriage
        List<TrainCarriage> trainCarriages = trainCarriageService.queryByTrainCode(train.getCode());
        for (TrainCarriage trainCarriage : trainCarriages) {
            DailyTrainCarriage dailyTrainCarriage = BeanUtil.copyProperties(trainCarriage, DailyTrainCarriage.class);
            dailyTrainCarriage.setDate(date);
            dailyTrainCarriage.setId(null); // 防止跑到修改去了
            dailyTrainCarriage.setCreateTime(null);
            dailyTrainCarriage.setUpdateTime(null);
            save(BeanUtil.copyProperties(dailyTrainCarriage, DailyTrainCarriageSaveForm.class));
        }
        log.info("已生成 【{}】 车次 【{}】 的所有每日车厢", DateUtil.format(date, "yyyy-MM-dd"), train.getCode());
    }

    @Override
    public List<DailyTrainCarriage> queryByDateAndTrainCodeAndIndex(Date date, String trainCode, Integer index) {
        DailyTrainCarriageExample dailyTrainCarriageExample = new DailyTrainCarriageExample();
        dailyTrainCarriageExample.setOrderByClause("date desc, train_code asc, `index` asc");
        dailyTrainCarriageExample.createCriteria()
                .andDateEqualTo(date)
                .andTrainCodeEqualTo(trainCode)
                .andIndexEqualTo(index);
        return dailyTrainCarriageMapper.selectByExample(dailyTrainCarriageExample);
    }

    @Override
    public List<DailyTrainCarriage> queryByDateAndTrainCode(Date date, String trainCode) {
        DailyTrainCarriageExample dailyTrainCarriageExample = new DailyTrainCarriageExample();
        dailyTrainCarriageExample.setOrderByClause("date desc, train_code asc, `index` asc");
        dailyTrainCarriageExample.createCriteria()
                .andDateEqualTo(date)
                .andTrainCodeEqualTo(trainCode);
        return dailyTrainCarriageMapper.selectByExample(dailyTrainCarriageExample);
    }
}
