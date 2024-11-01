package garry.train.business.controller;

import garry.train.business.feign.MemberFeign;
import garry.train.common.vo.ResponseVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Resource
    private MemberFeign memberFeign;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public ResponseVo<String> hello() {
//        memberFeign.hello();
        return ResponseVo.success("hello business");
    }
}
