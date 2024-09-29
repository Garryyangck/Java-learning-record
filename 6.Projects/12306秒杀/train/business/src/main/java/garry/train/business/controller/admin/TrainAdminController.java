package garry.train.business.controller.admin;

import garry.train.business.service.TrainSeatService;
import garry.train.business.service.TrainStationService;
import garry.train.business.vo.TrainQueryAllVo;
import garry.train.common.util.HostHolder;
import garry.train.common.vo.PageVo;
import garry.train.common.vo.ResponseVo;
import garry.train.business.form.TrainQueryForm;
import garry.train.business.form.TrainSaveForm;
import garry.train.business.service.TrainService;
import garry.train.business.vo.TrainQueryVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Garry
 * 2024-09-22 13:19
 */
@RestController
@RequestMapping(value = "/admin/train")
public class TrainAdminController {
    @Resource
    private TrainService trainService;

    @Resource
    private TrainStationService trainStationService;

    @Resource
    private TrainSeatService trainSeatService;

    @Resource
    private HostHolder hostHolder;

    /**
     * 接收新增和修改车次的请求，如果 form.id = null，则为新增；反之为修改
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseVo save(@Valid @RequestBody TrainSaveForm form) {
//        form.setMemberId(hostHolder.getMemberId());
        trainService.save(form);
        return ResponseVo.success();
    }

    @RequestMapping(value = "/query-list", method = RequestMethod.GET)
    public ResponseVo<PageVo<TrainQueryVo>> queryList(@Valid TrainQueryForm form) {
//        form.setMemberId(hostHolder.getMemberId()); // service 层是管理员和用户通用的接口，只有用户才需要取 memberId，因此取 memberId 的操作在 Controller 层实现
        PageVo<TrainQueryVo> vo = trainService.queryList(form);
        return ResponseVo.success(vo);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseVo delete(@PathVariable Long id) {
        trainService.delete(id);
        return ResponseVo.success();
    }

    @RequestMapping(value = "/query-all", method = RequestMethod.GET)
    public ResponseVo<List<TrainQueryAllVo>> queryAllCode() {
        List<TrainQueryAllVo> vo = trainService.queryAll();
        return ResponseVo.success(vo);
    }

    @RequestMapping(value = "/gen-train-seat/{trainCode}", method = RequestMethod.POST)
    public ResponseVo genTrainSeat(@PathVariable String trainCode) {
        trainSeatService.genTrainSeat(trainCode);
        return ResponseVo.success();
    }

    @RequestMapping(value = "/gen-train-station/{trainCode}", method = RequestMethod.POST)
    public ResponseVo genTrainStation(@PathVariable String trainCode) {
        trainStationService.genTrainStation(trainCode);
        return ResponseVo.success();
    }
}
