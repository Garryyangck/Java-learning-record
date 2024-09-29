package garry.train.business.controller.admin;

import garry.train.business.form.DailyTrainQueryForm;
import garry.train.business.form.DailyTrainSaveForm;
import garry.train.business.service.DailyTrainService;
import garry.train.business.vo.DailyTrainQueryVo;
import garry.train.common.util.HostHolder;
import garry.train.common.vo.PageVo;
import garry.train.common.vo.ResponseVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author Garry
 * 2024-09-28 16:33
 */
@RestController
@RequestMapping(value = "/admin/daily-train")
public class DailyTrainAdminController {
    @Resource
    private DailyTrainService dailyTrainService;

    @Resource
    private HostHolder hostHolder;

    /**
     * 接收新增和修改每日车次的请求，如果 form.id = null，则为新增；反之为修改
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseVo save(@Valid @RequestBody DailyTrainSaveForm form) {
//        form.setMemberId(hostHolder.getMemberId());
        dailyTrainService.save(form);
        return ResponseVo.success();
    }

    @RequestMapping(value = "/query-list", method = RequestMethod.GET)
    public ResponseVo<PageVo<DailyTrainQueryVo>> queryList(@Valid DailyTrainQueryForm form) {
        PageVo<DailyTrainQueryVo> vo = dailyTrainService.queryList(form);
        return ResponseVo.success(vo);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseVo delete(@PathVariable Long id) {
        dailyTrainService.delete(id);
        return ResponseVo.success();
    }

    @RequestMapping(value = "/gen-daily/{date}", method = RequestMethod.GET)
    public ResponseVo genDaily(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        dailyTrainService.genDaily(date);
        return ResponseVo.success();
    }
}
