package garry.train.business.controller.admin;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import garry.train.business.feign.MemberFeign;
import garry.train.common.form.ApiDetailQueryForm;
import garry.train.common.service.ApiDetailService;
import garry.train.common.util.CommonUtil;
import garry.train.common.util.PageUtil;
import garry.train.common.vo.ApiDetailVo;
import garry.train.common.vo.PageVo;
import garry.train.common.vo.ResponseVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Garry
 * 2024-10-14 21:25
 */
@RestController
@RequestMapping(value = "/admin/api-detail")
public class ApiDetailAdminController {

    @Resource
    private ApiDetailService apiDetailService;

    @Resource
    private MemberFeign memberFeign;

    @RequestMapping(value = "/query-list", method = RequestMethod.GET)
    public ResponseVo<PageVo<ApiDetailVo>> queryList(@Valid ApiDetailQueryForm form) {
        PageVo<ApiDetailVo> vo = apiDetailService.queryList(form);
        return ResponseVo.success(vo);
    }

    /**
     * 获取所有的模块的 ApiDetail
     */
    @RequestMapping(value = "/query-all-list", method = RequestMethod.GET)
    public ResponseVo<PageVo<ApiDetailVo>> queryAllList(@Valid ApiDetailQueryForm form) {

        List<ApiDetailVo> businessApiDetails = apiDetailService.queryList(form).getList();

        ResponseVo<String> responseVo = CommonUtil.getResponseVo(memberFeign.queryList(form.getPageNum(), form.getPageSize()));
        @SuppressWarnings("unchecked")
        List<Object> apiDetailJSONList = JSONUtil.parseObj(responseVo.getData()).get("list", List.class);
        List<ApiDetailVo> memberApiDetails = apiDetailJSONList.stream()
                .map(o -> {
                    if (o instanceof JSONObject apiDetailJSON) {
                        return JSONUtil.toBean(apiDetailJSON, ApiDetailVo.class);
                    }
                    return null;
                }).toList();

        businessApiDetails.addAll(memberApiDetails); // 合并
        PageInfo<ApiDetailVo> pageInfo = PageUtil.getPageInfo(businessApiDetails, form.getPageNum(), form.getPageSize());
        @SuppressWarnings("unchecked")
        PageVo<ApiDetailVo> vo = BeanUtil.copyProperties(pageInfo, PageVo.class);

        return ResponseVo.success(vo);
    }
}
