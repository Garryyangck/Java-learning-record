package garry.train.business.controller;

import garry.train.business.service.TrainService;
import garry.train.business.vo.TrainQueryAllVo;
import garry.train.common.util.HostHolder;
import garry.train.common.vo.ResponseVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Garry
 * 2024-09-22 13:19
 */
@RestController
@RequestMapping(value = "/train")
public class TrainController {
    @Resource
    private TrainService trainService;

    @Resource
    private HostHolder hostHolder;

    @RequestMapping(value = "/query-all", method = RequestMethod.GET)
    public ResponseVo<List<TrainQueryAllVo>> queryAllCode() {
        List<TrainQueryAllVo> vo = trainService.queryAll();
        return ResponseVo.success(vo);
    }
}
