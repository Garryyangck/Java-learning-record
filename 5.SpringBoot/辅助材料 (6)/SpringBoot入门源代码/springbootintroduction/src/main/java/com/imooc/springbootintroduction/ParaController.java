package com.imooc.springbootintroduction;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：     演示各种传参形式
 */
@RestController
//@Controller
//@ResponseBody
@RequestMapping("/uniprefix")
public class ParaController {

    @GetMapping({"/firstrequest"})
    public String firstrequest() {
        return "第一个Spring Boot接口";
    }

    @GetMapping({"/requestpara"})
    public String requestpara(@RequestParam Integer num) {
        return "para from request: " + num;
    }


    @GetMapping({"/para/{num}"})
    public String pathpara(@PathVariable Integer num) {
        return "para from path: " + num;
    }


    //    @RequestMapping("/prefix")
    @GetMapping({"/multiurl1", "/multiurl2"})
    public String multiurl(@RequestParam Integer num) {
        return "multiurl: " + num;
    }

    @GetMapping({"/required"})
    public String requiredAndDefault(
            @RequestParam(required = false, defaultValue = "0") Integer num) {
        return "para from request: " + num;
    }
}
