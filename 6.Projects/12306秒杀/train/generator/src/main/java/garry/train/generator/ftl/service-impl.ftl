package garry.train.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import garry.train.common.util.CommonUtil;
import garry.train.common.vo.PageVo;
import garry.train.member.form.${Domain}QueryForm;
import garry.train.member.form.${Domain}SaveForm;
import garry.train.member.mapper.${Domain}Mapper;
import garry.train.member.pojo.${Domain};
import garry.train.member.pojo.${Domain}Example;
import garry.train.member.service.${Domain}Service;
import garry.train.member.vo.${Domain}QueryVo;
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

        if (ObjectUtil.isNull(${domain}.getId())) { // 新增
            // 对Id、memberId、createTime、updateTime 重新赋值
            ${domain}.setId(CommonUtil.getSnowflakeNextId());
            ${domain}.setMemberId(form.getMemberId()); // 用户直接hostHolder获取memberId，管理员则是输入用户memberId
            ${domain}.setCreateTime(now);
            ${domain}.setUpdateTime(now);
            ${domain}Mapper.insert(${domain});
            log.info("新增乘客：{}", ${domain});
        } else { // 更新
            ${domain}.setUpdateTime(now);
            ${domain}Mapper.updateByPrimaryKeySelective(${domain});
            log.info("修改乘客：{}", ${domain});
        }
    }

    @Override
    public PageVo<${Domain}QueryVo> queryList(${Domain}QueryForm form) {
        Long memberId = form.getMemberId();
        ${Domain}Example ${domain}Example = new ${Domain}Example();
        ${domain}Example.setOrderByClause("update_time desc"); // 最新操作的乘客最上面，必须是数据库原始的字段名
        ${Domain}Example.Criteria criteria = ${domain}Example.createCriteria();

        // 只有用户，才只能查自己 memberId 下的乘客
        if (ObjectUtil.isNotNull(memberId)) {
            criteria.andMemberIdEqualTo(memberId);
        }

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
        vo.setMsg("queryList success");
        return vo;
    }

    @Override
    public void delete(Long id) {
        ${domain}Mapper.deleteByPrimaryKey(id);
    }
}
