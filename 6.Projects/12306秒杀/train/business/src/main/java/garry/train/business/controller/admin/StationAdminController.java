package garry.train.business.controller.admin;

import garry.train.business.form.StationQueryForm;
import garry.train.business.form.StationSaveForm;
import garry.train.business.service.StationService;
import garry.train.business.vo.StationQueryVo;
import garry.train.common.vo.PageVo;
import garry.train.common.vo.ResponseVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Garry
 * 2024-09-19 21:56
 */
@RestController
@RequestMapping(value = "/admin/station")
public class StationAdminController {
    @Resource
    private StationService stationService;

    /**
     * 接收新增和修改车站的请求，如果 form.id = null，则为新增；反之为修改
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseVo save(@Valid @RequestBody StationSaveForm form) {
//        form.setMemberId(hostHolder.getMemberId());
        stationService.save(form);
        stationService.queryAllRefreshCache(); // 更新 redis 缓存
        return ResponseVo.success();
    }

    @RequestMapping(value = "/query-list", method = RequestMethod.GET)
    public ResponseVo<PageVo<StationQueryVo>> queryList(@Valid StationQueryForm form) {
//        form.setMemberId(hostHolder.getMemberId()); // service 层是管理员和用户通用的接口，只有用户才需要取 memberId，因此取 memberId 的操作在 Controller 层实现
        PageVo<StationQueryVo> vo = stationService.queryList(form);
        return ResponseVo.success(vo);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseVo delete(@PathVariable Long id) {
        stationService.delete(id);
        stationService.queryAllRefreshCache(); // 更新 redis 缓存
        return ResponseVo.success();
    }

    @RequestMapping(value = "/query-all", method = RequestMethod.GET)
    public ResponseVo<List<StationQueryVo>> queryAll() {
        List<StationQueryVo> vo = stationService.queryAll();
        return ResponseVo.success(vo);
    }
}
