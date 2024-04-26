package garry.springbootlearn.controller;

/**
 * @author Garry
 * ---------2024/3/4 15:28
 **/

import org.springframework.web.bind.annotation.*;

/**
 * 演示传参形式
 */
@SuppressWarnings({"all"})
@RestController//在普通Controller的基础上有Restful的能力
@RequestMapping("/prefix")//统一前缀
public class ParamController {

    /**
     * 第一个Spring Boot接口
     *
     * @return
     */
    @GetMapping({"/firstRequest"})
    public String firstRequest() {
        return "第一个Spring Boot接口";
    }

    /**
     * 接收参数的Spring Boot接口
     *
     * @param num
     * @return
     */
    @GetMapping({"/requestParam"})
    public String requestParam(@RequestParam Integer num) {
        return "param from request:" + num;
    }

    /**
     * 从URL中寻找所对应的参数进行绑定
     *
     * @param num
     * @return
     */
    @GetMapping({"/pathParam/{num}"})//在path中获取参数
    public String pathParam(@PathVariable Integer num) {
        return "param from path:" + num;
    }

    /**
     * 支持多URL访问该方法
     *
     * @param num
     * @return
     */
    @GetMapping({"/multiUrl1", "/multiUrl2"})//当作是数组
    public String multiUrl(Integer num) {
        return "param from mutiPaths:" + num;
    }

    /**
     * 设置默认值，增强程序健壮性
     *
     * @param num
     * @return
     */
    @GetMapping({"/required"})
    public String required(@RequestParam(required = false/*不是必传*/, defaultValue = "0") Integer num) {
        return "the default value is:" + num;
    }
}
