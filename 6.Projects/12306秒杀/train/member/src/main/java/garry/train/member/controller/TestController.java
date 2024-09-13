package garry.train.member.controller;

import garry.train.common.vo.ResponseVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @RequestMapping(value = "hello", method = RequestMethod.GET)
    public ResponseVo<String> hello() {
        return ResponseVo.success("hello member");
    }
}
