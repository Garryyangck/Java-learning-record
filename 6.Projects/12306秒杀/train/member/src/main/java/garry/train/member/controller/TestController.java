package garry.train.member.controller;

import garry.train.common.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class TestController {

    @Value("${test.nacos}")
    private String testNacos;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public ResponseVo<String> hello() {
        return ResponseVo.success(testNacos);
    }
}
