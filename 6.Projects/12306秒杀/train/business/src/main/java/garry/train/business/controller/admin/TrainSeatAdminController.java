package garry.train.business.controller.admin;

import garry.train.business.form.TrainSeatQueryForm;
import garry.train.business.form.TrainSeatSaveForm;
import garry.train.business.service.TrainSeatService;
import garry.train.business.vo.TrainSeatQueryVo;
import garry.train.common.vo.PageVo;
import garry.train.common.vo.ResponseVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author Garry
 * 2024-09-22 21:13
 */
@RestController
@RequestMapping(value = "/admin/train-seat")
public class TrainSeatAdminController {
    @Resource
    private TrainSeatService trainSeatService;

    /**
     * 接收新增和修改座位的请求，如果 form.id = null，则为新增；反之为修改
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseVo save(@Valid @RequestBody TrainSeatSaveForm form) {
//        form.setMemberId(hostHolder.getMemberId());
        trainSeatService.save(form);
        return ResponseVo.success();
    }

    @RequestMapping(value = "/query-list", method = RequestMethod.GET)
    public ResponseVo<PageVo<TrainSeatQueryVo>> queryList(@Valid TrainSeatQueryForm form) {
//        form.setMemberId(hostHolder.getMemberId()); // service 层是管理员和用户通用的接口，只有用户才需要取 memberId，因此取 memberId 的操作在 Controller 层实现
        PageVo<TrainSeatQueryVo> vo = trainSeatService.queryList(form);
        return ResponseVo.success(vo);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseVo delete(@PathVariable Long id) {
        trainSeatService.delete(id);
        return ResponseVo.success();
    }
}
