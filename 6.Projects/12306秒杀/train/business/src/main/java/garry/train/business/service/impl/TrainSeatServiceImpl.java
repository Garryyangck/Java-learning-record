package garry.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import garry.train.business.enums.SeatColEnum;
import garry.train.business.form.TrainSeatQueryForm;
import garry.train.business.form.TrainSeatSaveForm;
import garry.train.business.mapper.TrainSeatMapper;
import garry.train.business.pojo.TrainCarriage;
import garry.train.business.pojo.TrainSeat;
import garry.train.business.pojo.TrainSeatExample;
import garry.train.business.service.TrainCarriageService;
import garry.train.business.service.TrainSeatService;
import garry.train.business.service.TrainService;
import garry.train.business.vo.TrainSeatQueryVo;
import garry.train.common.enums.ResponseEnum;
import garry.train.common.exception.BusinessException;
import garry.train.common.util.CommonUtil;
import garry.train.common.vo.PageVo;
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
    private TrainService trainService;

    @Resource
    private TrainCarriageService trainCarriageService;

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
        trainSeatExample.setOrderByClause("train_code asc, carriage_index asc, row asc, carriage_seat_index asc");
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

    @Override
    public void deleteByTrainCode(String trainCode) {
        TrainSeatExample trainSeatExample = new TrainSeatExample();
        trainSeatExample.createCriteria().andTrainCodeEqualTo(trainCode);
        trainSeatMapper.deleteByExample(trainSeatExample);
    }

    @Override
    public List<TrainSeat> queryByTrainCodeAndCarriageIndex(String trainCode, Integer carriageIndex) {
        TrainSeatExample trainSeatExample = new TrainSeatExample();
        trainSeatExample.createCriteria()
                .andTrainCodeEqualTo(trainCode)
                .andCarriageIndexEqualTo(carriageIndex);
        return trainSeatMapper.selectByExample(trainSeatExample);
    }

    @Override
    public List<TrainSeat> queryByTrainCode(String trainCode) {
        TrainSeatExample trainSeatExample = new TrainSeatExample();
        trainSeatExample.createCriteria()
                .andTrainCodeEqualTo(trainCode);
        return trainSeatMapper.selectByExample(trainSeatExample);
    }

    @Override
    public void genTrainSeat(String trainCode) {
        // 检查参数
        if (StrUtil.isBlank(trainCode)
                || trainService.queryByCode(trainCode).isEmpty()) {
            throw new BusinessException(ResponseEnum.BUSINESS_WRONG_TRAIN_CODE);
        }

        // 清空 trainCode 的所有已有座位
        deleteByTrainCode(trainCode);

        // 获取 trainCode 下的所有车厢
        List<TrainCarriage> carriages = trainCarriageService.queryByTrainCode(trainCode);

        // 遍历生成每一个车厢的座位
        for (TrainCarriage carriage : carriages) {
            Integer carriageIndex = carriage.getIndex();
            String seatType = carriage.getSeatType();
            for (int row = 1; row <= carriage.getRowCount(); row++) {
                List<SeatColEnum> colEnums = SeatColEnum.getColsByType(seatType);
                for (SeatColEnum colEnum : colEnums) {
                    String col = colEnum.getCode();
                    TrainSeatSaveForm form = genTrainSeatSaveForm(trainCode, colEnum, carriageIndex, row, col, seatType, colEnums);
                    save(form);
                }
            }
        }
    }

    private static TrainSeatSaveForm genTrainSeatSaveForm(String trainCode, SeatColEnum colEnum, Integer carriageIndex, int row, String col, String seatType, List<SeatColEnum> colEnums) {
        TrainSeatSaveForm form = new TrainSeatSaveForm();
        form.setTrainCode(trainCode);
        form.setCarriageIndex(carriageIndex);
        form.setRow(row < 10 ? "0" + row : String.valueOf(row));
        form.setCol(col);
        form.setSeatType(seatType);
        form.setCarriageSeatIndex((row - 1) * colEnums.size() + colEnums.indexOf(colEnum) + 1);
        return form;
    }
}
