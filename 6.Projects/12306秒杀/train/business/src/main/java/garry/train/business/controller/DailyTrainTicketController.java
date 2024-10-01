package garry.train.business.controller;

import garry.train.business.form.DailyTrainTicketQueryForm;
import garry.train.business.service.DailyTrainTicketService;
import garry.train.business.vo.DailyTrainTicketQueryVo;
import garry.train.common.util.HostHolder;
import garry.train.common.vo.PageVo;
import garry.train.common.vo.ResponseVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Garry
 * 2024-09-30 14:51
 */
@RestController
@RequestMapping(value = "/daily-train-ticket")
public class DailyTrainTicketController {
    @Resource
    private DailyTrainTicketService dailyTrainTicketService;

    @Resource
    private HostHolder hostHolder;

    @RequestMapping(value = "/query-list", method = RequestMethod.GET)
    public ResponseVo<PageVo<DailyTrainTicketQueryVo>> queryList(@Valid DailyTrainTicketQueryForm form) {
        PageVo<DailyTrainTicketQueryVo> vo = dailyTrainTicketService.queryList(form);
        return ResponseVo.success(vo);
    }
}
