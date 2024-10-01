package garry.train.business.controller.admin;

import garry.train.business.form.DailyTrainStationQueryForm;
import garry.train.business.form.DailyTrainStationSaveForm;
import garry.train.business.service.DailyTrainStationService;
import garry.train.business.vo.DailyTrainStationQueryVo;
import garry.train.common.vo.PageVo;
import garry.train.common.vo.ResponseVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author Garry
 * 2024-09-28 19:32
 */
@RestController
@RequestMapping(value = "/admin/daily-train-station")
public class DailyTrainStationAdminController {
    @Resource
    private DailyTrainStationService dailyTrainStationService;

    /**
     * 接收新增和修改每日车站的请求，如果 form.id = null，则为新增；反之为修改
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseVo save(@Valid @RequestBody DailyTrainStationSaveForm form) {
//        form.setMemberId(hostHolder.getMemberId());
        dailyTrainStationService.save(form);
        return ResponseVo.success();
    }

    @RequestMapping(value = "/query-list", method = RequestMethod.GET)
    public ResponseVo<PageVo<DailyTrainStationQueryVo>> queryList(@Valid DailyTrainStationQueryForm form) {
        PageVo<DailyTrainStationQueryVo> vo = dailyTrainStationService.queryList(form);
        return ResponseVo.success(vo);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseVo delete(@PathVariable Long id) {
        dailyTrainStationService.delete(id);
        return ResponseVo.success();
    }
}
