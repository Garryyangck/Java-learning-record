package garry.springmvc.controller;

import garry.springmvc.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Garry
 * ---------2024/3/3 16:24
 **/
@Controller
@RequestMapping("/um")
public class URLMappingController {
    @GetMapping("/g")
    @ResponseBody
    //传来的是user_name，接收时映射为驼峰命名法
    public String getMapping(@RequestParam("user_name") String userName) {
        return userName;
    }

    @GetMapping("/gm")
    @ResponseBody
    public String getMapping1(String username, String password, Date createTime) {
        System.out.println(username);
        return username + ":" + password + ":" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(createTime);
    }

    @PostMapping("/pm")
    @ResponseBody
    public String postMapping1(String username, String password, Date createTime) {
        System.out.println(username);
        return username + ":" + password + ":" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(createTime);
    }

    @GetMapping("/view")
    public ModelAndView showView(Integer userId) {
        ModelAndView mav = new ModelAndView("redirect:/view.jsp");
        mav.setViewName("view.jsp");//相对路径，当前路径是webapp/um，因此如果需要使用相对路径
        //需要在webapp中创建一个um目录，里面有一个view.jsp
        User user = new User();
        if (1 == userId) {
            user.setUsername("zhangsan");
        } else if (2 == userId) {
            user.setUsername("lisi");
        }
        mav.addObject("user", user);
        return mav;
    }

    public String showView1(Integer userId, ModelMap modelMap) {
        String view = "/um/view.jsp";
        User user = new User();
        if (1 == userId) {
            user.setUsername("zhangsan");
        } else if (2 == userId) {
            user.setUsername("lisi");
        }
        modelMap.addAttribute("user", user);
        return view;
    }
}
