package garry.train.business.controller.admin;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
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

import java.math.BigDecimal;
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
        businessApiDetails = businessApiDetails.stream()
                // 过滤
                .filter(apiDetailVo -> {
                    Boolean judgeApiMethod = StrUtil.isBlank(form.getApiMethod()) || form.getApiMethod().equals(apiDetailVo.getApiMethod());
                    Boolean judgeModuleName = StrUtil.isBlank(form.getModuleName()) || form.getModuleName().equals(apiDetailVo.getModuleName());
                    return judgeApiMethod && judgeModuleName;
                // 排序
                }).sorted((apiDetailVo1, apiDetailVo2) -> {
                    // 按多个字段排序的思路：传来一个字符串数组，遍历数组，先遍历到的优先级更高
                    // 如果高优先级字段相同，则比较下一个优先级的字段，否则直接可以返回了
                    int sortNum = 0;
                    boolean _isAsc = ObjUtil.isNotNull(form.getIsAsc()) ? form.getIsAsc() : false;
                    if (StrUtil.isNotBlank(form.getSortBy())) {
                        switch (form.getSortBy()) {
                            case "callTimes":
                                sortNum = apiDetailVo1.getCallTimes().compareTo(apiDetailVo2.getCallTimes());
                                break;
                            case "successTimes":
                                sortNum = apiDetailVo1.getSuccessTimes().compareTo(apiDetailVo2.getSuccessTimes());
                                break;
                            case "successRatio":
                                // 注意：百分数 "33.33%" 不能使用 compareTo，需要转换为 BigDecimal 比较
                                BigDecimal successRatio1 = extractRatioAsDecimal(apiDetailVo1.getSuccessRatio());
                                BigDecimal successRatio2 = extractRatioAsDecimal(apiDetailVo2.getSuccessRatio());
                                sortNum = successRatio1.compareTo(successRatio2);
                                break;
                            case "maxExecuteMills":
                                sortNum = apiDetailVo1.getMaxExecuteMills().compareTo(apiDetailVo2.getMaxExecuteMills());
                                break;
                            case "minExecuteMills":
                                sortNum = apiDetailVo1.getMinExecuteMills().compareTo(apiDetailVo2.getMinExecuteMills());
                                break;
                            case "executeMills":
                                sortNum = apiDetailVo1.getExecuteMills().compareTo(apiDetailVo2.getExecuteMills());
                                break;
                            case "successExecuteMills":
                                sortNum = apiDetailVo1.getSuccessExecuteMills().compareTo(apiDetailVo2.getSuccessExecuteMills());
                                break;
                            case "avgExecuteMills":
                                sortNum = apiDetailVo1.getAvgExecuteMills().compareTo(apiDetailVo2.getAvgExecuteMills());
                                break;
                            case "avgSuccessExecuteMills":
                                sortNum = apiDetailVo1.getAvgSuccessExecuteMills().compareTo(apiDetailVo2.getAvgSuccessExecuteMills());
                                break;
                            default:
                                break;
                        }
                    }
                    return (_isAsc ? 1 : -1) * sortNum;
                }).toList();
        PageInfo<ApiDetailVo> pageInfo = PageUtil.getPageInfo(businessApiDetails, form.getPageNum(), form.getPageSize());
        @SuppressWarnings("unchecked")
        PageVo<ApiDetailVo> vo = BeanUtil.copyProperties(pageInfo, PageVo.class);

        return ResponseVo.success(vo);
    }

    /**
     * 提取成功比例中的数值部分并转换为 BigDecimal
     */
    private static BigDecimal extractRatioAsDecimal(String ratio) {
        if (StrUtil.isBlank(ratio)) {
            return BigDecimal.ZERO;
        }
        String numericPart = ratio.replaceAll("[^\\d.]", ""); // 移除非数字和非点字符
        return new BigDecimal(numericPart);
    }
}
