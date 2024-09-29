package garry.train.batch.controller;

import cn.hutool.json.JSONUtil;
import garry.train.batch.feign.BusinessFeign;
import garry.train.common.util.CommonUtil;
import garry.train.common.vo.ResponseVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Resource
    private BusinessFeign businessFeign;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public ResponseVo<String> hello() {
        String responseVoStr = businessFeign.queryAll();
        System.out.println(responseVoStr);
        ResponseVo<String> responseVo = CommonUtil.getResponseVo(responseVoStr);
        System.out.println(JSONUtil.toJsonPrettyStr(responseVo));
        return ResponseVo.success("responseVo = " + responseVo);
    }


}
