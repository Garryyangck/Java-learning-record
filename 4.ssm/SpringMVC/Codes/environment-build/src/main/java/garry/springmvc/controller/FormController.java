package garry.springmvc.controller;

import garry.springmvc.entity.Form;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author Garry
 * ---------2024/3/3 18:34
 **/
@Controller
public class FormController {
/*    @RequestMapping(value = "/apply")
    @ResponseBody
    public String apply(@RequestParam(value = "userName", defaultValue = "defaultValue")
                                String name, String course, @RequestParam List<Integer> purpose) {
        System.out.println(name);//表单中不存在userName，因此参数name为默认值
        System.out.println(course);
        for (Integer value : purpose) {
            System.out.println(value);
        }
        return "SUCCEED";
    }*/

    @RequestMapping(value = "/apply")
    @ResponseBody
    public String apply(Form form) {
        System.out.println(form);
        return "SUCCEED";
    }
}
