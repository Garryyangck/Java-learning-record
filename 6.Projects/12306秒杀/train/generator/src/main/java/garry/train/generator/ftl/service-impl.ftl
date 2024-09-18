package garry.train.${module}.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import garry.train.common.util.CommonUtil;
import garry.train.common.vo.PageVo;
import garry.train.${module}.form.${Domain}QueryForm;
import garry.train.${module}.form.${Domain}SaveForm;
import garry.train.${module}.mapper.${Domain}Mapper;
import garry.train.${module}.pojo.${Domain};
import garry.train.${module}.pojo.${Domain}Example;
import garry.train.${module}.service.${Domain}Service;
import garry.train.${module}.vo.${Domain}QueryVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Garry
 * ${DateTime}
 */
@Slf4j
@Service
public class ${Domain}ServiceImpl implements ${Domain}Service {
    @Resource
    private ${Domain}Mapper ${domain}Mapper;

    @Override
    public void save(${Domain}SaveForm form) {
        ${Domain} ${domain} = BeanUtil.copyProperties(form, ${Domain}.class);
        DateTime now = DateTime.now();

        if (ObjectUtil.isNull(${domain}.getId())) { // 插入
            // 对Id、createTime、updateTime 重新赋值
            // 可能还需要重新赋值其它的字段，比如 Passenger.memberId
            ${domain}.setId(CommonUtil.getSnowflakeNextId());
            ${domain}.setCreateTime(now);
            ${domain}.setUpdateTime(now);
            ${domain}Mapper.insert(${domain});
            log.info("插入${tableNameCn}：{}", ${domain});
        } else { // 修改
            ${domain}.setUpdateTime(now);
            ${domain}Mapper.updateByPrimaryKeySelective(${domain});
            log.info("修改${tableNameCn}：{}", ${domain});
        }
    }

    @Override
    public PageVo<${Domain}QueryVo> queryList(${Domain}QueryForm form) {
        ${Domain}Example ${domain}Example = new ${Domain}Example();
        ${domain}Example.setOrderByClause("update_time desc"); // 最新更新的数据，最先被查出来
        ${Domain}Example.Criteria criteria = ${domain}Example.createCriteria();
        // 这里自定义一些过滤的条件，比如:
//        // 用户只能查自己 memberId 下的${tableNameCn}
//        if (ObjectUtil.isNotNull()) {
//            criteria.andMemberIdEqualTo(memberId);
//        }

        // 启动分页
        PageHelper.startPage(form.getPageNum(), form.getPageSize());

        // 获取 ${domain}s
        List<${Domain}> ${domain}s = ${domain}Mapper.selectByExample(${domain}Example);

        // 获得 pageInfo 对象，并将其 List 的模板类型改为 ${Domain}QueryVo
        // 注意这里必须先获取 pageInfo，再尝试获取 List<${Domain}QueryVo>，否则无法正确获取 pageNum，pages 等重要属性
        PageInfo<${Domain}> pageInfo = new PageInfo<>(${domain}s);
        List<${Domain}QueryVo> voList = BeanUtil.copyToList(pageInfo.getList(), ${Domain}QueryVo.class);

        // 获取 PageVo 对象
        PageVo<${Domain}QueryVo> vo = BeanUtil.copyProperties(pageInfo, PageVo.class);
        vo.setList(voList);
        vo.setMsg("查询${tableNameCn}列表成功");
        return vo;
    }

    @Override
    public void delete(Long id) {
        ${domain}Mapper.deleteByPrimaryKey(id);
    }
}
