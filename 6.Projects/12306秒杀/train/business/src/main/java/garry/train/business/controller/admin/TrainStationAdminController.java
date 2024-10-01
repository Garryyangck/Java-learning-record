package garry.train.business.controller.admin;

import garry.train.business.form.TrainStationQueryForm;
import garry.train.business.form.TrainStationSaveForm;
import garry.train.business.service.TrainStationService;
import garry.train.business.vo.TrainStationQueryVo;
import garry.train.common.vo.PageVo;
import garry.train.common.vo.ResponseVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author Garry
 * 2024-09-22 14:22
 */
@RestController
@RequestMapping(value = "/admin/train-station")
public class TrainStationAdminController {
    @Resource
    private TrainStationService trainStationService;

    /**
     * 接收新增和修改火车车站的请求，如果 form.id = null，则为新增；反之为修改
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseVo save(@Valid @RequestBody TrainStationSaveForm form) {
//        form.setMemberId(hostHolder.getMemberId());
        trainStationService.save(form);
        return ResponseVo.success();
    }

    @RequestMapping(value = "/query-list", method = RequestMethod.GET)
    public ResponseVo<PageVo<TrainStationQueryVo>> queryList(@Valid TrainStationQueryForm form) {
//        form.setMemberId(hostHolder.getMemberId()); // service 层是管理员和用户通用的接口，只有用户才需要取 memberId，因此取 memberId 的操作在 Controller 层实现
        PageVo<TrainStationQueryVo> vo = trainStationService.queryList(form);
        return ResponseVo.success(vo);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseVo delete(@PathVariable Long id) {
        trainStationService.delete(id);
        return ResponseVo.success();
    }
}
