package garry.train.business.controller.admin;

import garry.train.common.util.HostHolder;
import garry.train.common.vo.PageVo;
import garry.train.common.vo.ResponseVo;
import garry.train.business.form.DailyTrainCarriageQueryForm;
import garry.train.business.form.DailyTrainCarriageSaveForm;
import garry.train.business.service.DailyTrainCarriageService;
import garry.train.business.vo.DailyTrainCarriageQueryVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author Garry
 * 2024-09-28 20:20
 */
@RestController
@RequestMapping(value = "/admin/daily-train-carriage")
public class DailyTrainCarriageAdminController {
    @Resource
    private DailyTrainCarriageService dailyTrainCarriageService;

    @Resource
    private HostHolder hostHolder;

    /**
     * 接收新增和修改每日车厢的请求，如果 form.id = null，则为新增；反之为修改
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseVo save(@Valid @RequestBody DailyTrainCarriageSaveForm form) {
//        form.setMemberId(hostHolder.getMemberId());
        dailyTrainCarriageService.save(form);
        return ResponseVo.success();
    }

    @RequestMapping(value = "/query-list", method = RequestMethod.GET)
    public ResponseVo<PageVo<DailyTrainCarriageQueryVo>> queryList(@Valid DailyTrainCarriageQueryForm form) {
        PageVo<DailyTrainCarriageQueryVo> vo = dailyTrainCarriageService.queryList(form);
        return ResponseVo.success(vo);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseVo delete(@PathVariable Long id) {
        dailyTrainCarriageService.delete(id);
        return ResponseVo.success();
    }
}
