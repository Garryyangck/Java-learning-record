package garry.train.${module}.controller;

import garry.train.common.util.HostHolder;
import garry.train.common.vo.PageVo;
import garry.train.common.vo.ResponseVo;
import garry.train.${module}.form.${Domain}QueryForm;
import garry.train.${module}.form.${Domain}SaveForm;
import garry.train.${module}.service.${Domain}Service;
import garry.train.${module}.vo.${Domain}QueryVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author Garry
 * ${DateTime}
 */
@RestController
@RequestMapping(value = "/${do_main}")
public class ${Domain}Controller {
    @Resource
    private ${Domain}Service ${domain}Service;

    @Resource
    private HostHolder hostHolder;

    /**
     * 接收新增和修改${tableNameCn}的请求，如果 form.id = null，则为新增；反之为修改
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseVo save(@Valid @RequestBody ${Domain}SaveForm form) {
//        form.setMemberId(hostHolder.getMemberId());
        ${domain}Service.save(form);
        return ResponseVo.success();
    }

    @RequestMapping(value = "/query-list", method = RequestMethod.GET)
    public ResponseVo<PageVo<${Domain}QueryVo>> queryList(@Valid ${Domain}QueryForm form) {
//        form.setMemberId(hostHolder.getMemberId()); // service 层是管理员和用户通用的接口，只有用户才需要取 memberId，因此取 memberId 的操作在 Controller 层实现
        PageVo<${Domain}QueryVo> vo = ${domain}Service.queryList(form);
        return ResponseVo.success(vo);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseVo delete(@PathVariable Long id) {
        ${domain}Service.delete(id);
        return ResponseVo.success();
    }
}
