package garry.train.business.controller.admin;

import garry.train.common.util.HostHolder;
import garry.train.common.vo.PageVo;
import garry.train.common.vo.ResponseVo;
import garry.train.business.form.DailyTrainSeatQueryForm;
import garry.train.business.form.DailyTrainSeatSaveForm;
import garry.train.business.service.DailyTrainSeatService;
import garry.train.business.vo.DailyTrainSeatQueryVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author Garry
 * 2024-09-28 20:53
 */
@RestController
@RequestMapping(value = "/admin/daily-train-seat")
public class DailyTrainSeatAdminController {
    @Resource
    private DailyTrainSeatService dailyTrainSeatService;

    @Resource
    private HostHolder hostHolder;

    /**
     * 接收新增和修改每日座位的请求，如果 form.id = null，则为新增；反之为修改
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseVo save(@Valid @RequestBody DailyTrainSeatSaveForm form) {
//        form.setMemberId(hostHolder.getMemberId());
        dailyTrainSeatService.save(form);
        return ResponseVo.success();
    }

    @RequestMapping(value = "/query-list", method = RequestMethod.GET)
    public ResponseVo<PageVo<DailyTrainSeatQueryVo>> queryList(@Valid DailyTrainSeatQueryForm form) {
        PageVo<DailyTrainSeatQueryVo> vo = dailyTrainSeatService.queryList(form);
        return ResponseVo.success(vo);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseVo delete(@PathVariable Long id) {
        dailyTrainSeatService.delete(id);
        return ResponseVo.success();
    }
}
