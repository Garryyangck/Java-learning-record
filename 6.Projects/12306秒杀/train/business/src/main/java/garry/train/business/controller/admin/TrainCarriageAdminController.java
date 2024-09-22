package garry.train.business.controller.admin;

import garry.train.common.util.HostHolder;
import garry.train.common.vo.PageVo;
import garry.train.common.vo.ResponseVo;
import garry.train.business.form.TrainCarriageQueryForm;
import garry.train.business.form.TrainCarriageSaveForm;
import garry.train.business.service.TrainCarriageService;
import garry.train.business.vo.TrainCarriageQueryVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author Garry
 * 2024-09-22 20:48
 */
@RestController
@RequestMapping(value = "/admin/train-carriage")
public class TrainCarriageAdminController {
    @Resource
    private TrainCarriageService trainCarriageService;

    @Resource
    private HostHolder hostHolder;

    /**
     * 接收新增和修改火车车厢的请求，如果 form.id = null，则为新增；反之为修改
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseVo save(@Valid @RequestBody TrainCarriageSaveForm form) {
//        form.setMemberId(hostHolder.getMemberId());
        trainCarriageService.save(form);
        return ResponseVo.success();
    }

    @RequestMapping(value = "/query-list", method = RequestMethod.GET)
    public ResponseVo<PageVo<TrainCarriageQueryVo>> queryList(@Valid TrainCarriageQueryForm form) {
//        form.setMemberId(hostHolder.getMemberId()); // service 层是管理员和用户通用的接口，只有用户才需要取 memberId，因此取 memberId 的操作在 Controller 层实现
        PageVo<TrainCarriageQueryVo> vo = trainCarriageService.queryList(form);
        return ResponseVo.success(vo);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseVo delete(@PathVariable Long id) {
        trainCarriageService.delete(id);
        return ResponseVo.success();
    }
}
