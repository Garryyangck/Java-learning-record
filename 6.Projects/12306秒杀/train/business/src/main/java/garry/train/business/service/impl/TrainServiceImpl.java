package garry.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import garry.train.common.util.CommonUtil;
import garry.train.common.vo.PageVo;
import garry.train.business.form.TrainQueryForm;
import garry.train.business.form.TrainSaveForm;
import garry.train.business.mapper.TrainMapper;
import garry.train.business.pojo.Train;
import garry.train.business.pojo.TrainExample;
import garry.train.business.service.TrainService;
import garry.train.business.vo.TrainQueryVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
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
    private TrainMapper trainMapper;

    @Override
    public void save(TrainSaveForm form) {
        Train train = BeanUtil.copyProperties(form, Train.class);
        DateTime now = DateTime.now();

        if (ObjectUtil.isNull(train.getId())) { // 插入
            // 对Id、createTime、updateTime 重新赋值
            // 可能还需要重新赋值其它的字段，比如 Passenger.memberId
            train.setId(CommonUtil.getSnowflakeNextId());
            train.setCreateTime(now);
            train.setUpdateTime(now);
            trainMapper.insert(train);
            log.info("插入车次：{}", train);
        } else { // 修改
            train.setUpdateTime(now);
            trainMapper.updateByPrimaryKeySelective(train);
            log.info("修改车次：{}", train);
        }
    }

    @Override
    public PageVo<TrainQueryVo> queryList(TrainQueryForm form) {
        TrainExample trainExample = new TrainExample();
        trainExample.setOrderByClause("update_time desc"); // 最新更新的数据，最先被查出来
        TrainExample.Criteria criteria = trainExample.createCriteria();
        // 这里自定义一些过滤的条件，比如:
//        // 用户只能查自己 memberId 下的车次
//        if (ObjectUtil.isNotNull()) {
//            criteria.andMemberIdEqualTo(memberId);
//        }

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
        trainMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<TrainQueryVo> queryAllCode() {
        TrainExample trainExample = new TrainExample();
        trainExample.setOrderByClause("code");
        List<Train> trains = trainMapper.selectByExample(trainExample);
        List<HashMap<String, String>> codes = trains.stream()
                .map(train -> {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("code", train.getCode());
                    return map;
                })
                .toList();

        return BeanUtil.copyToList(codes, TrainQueryVo.class);
    }
}
