package garry.train.member.controller.admin;

import garry.train.common.form.ApiDetailQueryForm;
import garry.train.common.service.ApiDetailService;
import garry.train.common.vo.ApiDetailVo;
import garry.train.common.vo.PageVo;
import garry.train.common.vo.ResponseVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Garry
 * 2024-10-14 21:25
 */
@RestController
@RequestMapping(value = "/admin/api-detail")
public class ApiDetailAdminController {

    @Resource
    private ApiDetailService apiDetailService;

    @RequestMapping(value = "/query-list", method = RequestMethod.GET)
    public ResponseVo<PageVo<ApiDetailVo>> queryList(@Valid ApiDetailQueryForm form) {
        PageVo<ApiDetailVo> vo = apiDetailService.queryList(form);
        return ResponseVo.success(vo);
    }
}
