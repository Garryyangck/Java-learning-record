package garry.train.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageInfo;
import garry.train.common.form.ApiDetailQueryForm;
import garry.train.common.service.ApiDetailService;
import garry.train.common.util.ApiDetail;
import garry.train.common.util.PageUtil;
import garry.train.common.vo.ApiDetailVo;
import garry.train.common.vo.PageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Garry
 * 2024-10-14 21:26
 */
@Slf4j
@Service
public class ApiDetailServiceImpl implements ApiDetailService {

    @Override
    public PageVo<ApiDetailVo> queryList(ApiDetailQueryForm form) {

        List<ApiDetail> apiDetails = ApiDetail.getApiDetailMap().values().stream().toList();

        // 创建 PageInfo 对象
        PageInfo<ApiDetail> pageInfo = PageUtil.getPageInfo(apiDetails, form.getPageNum(), form.getPageSize());

        // 创建 List<ApiDetailVo>
        List<ApiDetailVo> voList = BeanUtil.copyToList(pageInfo.getList(), ApiDetailVo.class);

        // 获取 PageVo 对象
        PageVo<ApiDetailVo> vo = BeanUtil.copyProperties(pageInfo, PageVo.class);
        vo.setList(voList);
        vo.setMsg("查询接口详情成功");
        return vo;
    }
}
