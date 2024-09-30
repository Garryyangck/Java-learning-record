package garry.train.business.controller.admin;

import garry.train.common.util.HostHolder;
import garry.train.common.vo.PageVo;
import garry.train.common.vo.ResponseVo;
import garry.train.business.form.DailyTrainTicketQueryForm;
import garry.train.business.form.DailyTrainTicketSaveForm;
import garry.train.business.service.DailyTrainTicketService;
import garry.train.business.vo.DailyTrainTicketQueryVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author Garry
 * 2024-09-30 14:51
 */
@RestController
@RequestMapping(value = "/admin/daily-train-ticket")
public class DailyTrainTicketAdminController {
    @Resource
    private DailyTrainTicketService dailyTrainTicketService;

    @Resource
    private HostHolder hostHolder;

    /**
     * 接收新增和修改余票信息的请求，如果 form.id = null，则为新增；反之为修改
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseVo save(@Valid @RequestBody DailyTrainTicketSaveForm form) {
        dailyTrainTicketService.save(form);
        return ResponseVo.success();
    }

    @RequestMapping(value = "/query-list", method = RequestMethod.GET)
    public ResponseVo<PageVo<DailyTrainTicketQueryVo>> queryList(@Valid DailyTrainTicketQueryForm form) {
        PageVo<DailyTrainTicketQueryVo> vo = dailyTrainTicketService.queryList(form);
        return ResponseVo.success(vo);
    }
}
