package garry.train.business.controller;

import garry.train.business.service.StationService;
import garry.train.business.vo.StationQueryVo;
import garry.train.common.util.HostHolder;
import garry.train.common.vo.ResponseVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Garry
 * 2024-09-19 21:56
 */
@RestController
@RequestMapping(value = "/station")
public class StationController {
    @Resource
    private StationService stationService;

    @Resource
    private HostHolder hostHolder;

    @RequestMapping(value = "/query-all", method = RequestMethod.GET)
    public ResponseVo<List<StationQueryVo>> queryAll() {
        List<StationQueryVo> vo = stationService.queryAll();
        return ResponseVo.success(vo);
    }
}
