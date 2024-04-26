package garry.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Garry
 * ---------2024/3/3 15:44
 **/
@Controller
public class TestController {
    @GetMapping("/test")//类似于@Servlet("/xxxServlet")
    @ResponseBody//直接向响应输出字符串数据，不跳转页面
    public String test() {
        return "Hello SpringMVC!!!!!!!!!!!!!!!";
    }
}
