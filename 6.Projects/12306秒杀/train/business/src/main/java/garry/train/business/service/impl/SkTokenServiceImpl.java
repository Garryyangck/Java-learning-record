package garry.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import garry.train.common.util.CommonUtil;
import garry.train.common.vo.PageVo;
import garry.train.business.form.SkTokenQueryForm;
import garry.train.business.form.SkTokenSaveForm;
import garry.train.business.mapper.SkTokenMapper;
import garry.train.business.pojo.SkToken;
import garry.train.business.pojo.SkTokenExample;
import garry.train.business.service.SkTokenService;
import garry.train.business.vo.SkTokenQueryVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Garry
 * 2024-11-05 18:16
 */
@Slf4j
@Service
public class SkTokenServiceImpl implements SkTokenService {
    @Resource
    private SkTokenMapper skTokenMapper;

    @Override
    public void save(SkTokenSaveForm form) {
        SkToken skToken = BeanUtil.copyProperties(form, SkToken.class);
        DateTime now = DateTime.now();

        if (ObjectUtil.isNull(skToken.getId())) { // 插入
            // 插入时要看数据库有没有唯一键约束，在此校验唯一键约束，防止出现 DuplicationKeyException

            // 对Id、createTime、updateTime 重新赋值
            // 可能还需要重新赋值其它的字段，比如 Passenger.memberId
            skToken.setId(CommonUtil.getSnowflakeNextId());
            skToken.setCreateTime(now);
            skToken.setUpdateTime(now);
            skTokenMapper.insert(skToken);
            log.info("插入秒杀令牌：{}", skToken);
        } else { // 修改
            skToken.setUpdateTime(now);
            skTokenMapper.updateByPrimaryKeySelective(skToken);
            log.info("修改秒杀令牌：{}", skToken);
        }
    }

    @Override
    public PageVo<SkTokenQueryVo> queryList(SkTokenQueryForm form) {
        SkTokenExample skTokenExample = new SkTokenExample();
        skTokenExample.setOrderByClause("update_time desc"); // 最新更新的数据，最先被查出来
        SkTokenExample.Criteria criteria = skTokenExample.createCriteria();
        // 这里自定义一些过滤的条件，比如:
//        // 用户只能查自己 memberId 下的秒杀令牌
//        if (ObjectUtil.isNotNull()) {
//            criteria.andMemberIdEqualTo(memberId);
//        }

        // 启动分页
        PageHelper.startPage(form.getPageNum(), form.getPageSize());

        // 获取 skTokens
        List<SkToken> skTokens = skTokenMapper.selectByExample(skTokenExample);

        // 获得 pageInfo 对象，并将其 List 的模板类型改为 SkTokenQueryVo
        // 注意这里必须先获取 pageInfo，再尝试获取 List<SkTokenQueryVo>，否则无法正确获取 pageNum，pages 等重要属性
        PageInfo<SkToken> pageInfo = new PageInfo<>(skTokens);
        List<SkTokenQueryVo> voList = BeanUtil.copyToList(pageInfo.getList(), SkTokenQueryVo.class);

        // 获取 PageVo 对象
        PageVo<SkTokenQueryVo> vo = BeanUtil.copyProperties(pageInfo, PageVo.class);
        vo.setList(voList);
        vo.setMsg("查询秒杀令牌列表成功");
        return vo;
    }

    @Override
    public void delete(Long id) {
        skTokenMapper.deleteByPrimaryKey(id);
    }
}
