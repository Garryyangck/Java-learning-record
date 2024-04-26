package garry.community.controller;

import garry.community.annotation.LoginRequired;
import garry.community.service.DataService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author Garry
 * ---------2024/3/29 21:44
 **/
@Controller
public class DataController {
    @Resource
    private DataService dataService;

    /**
     * 显示数据统计页面，并支持接收转发
     *
     * @return
     */
    @LoginRequired
    @RequestMapping(value = "/data", method = {RequestMethod.GET, RequestMethod.POST/*接收的转发请求是POST*/})
    public String getDatePage() {
        return "/site/admin/data";
    }

    /**
     * 统计uv，转发给/data
     *
     * @param model
     * @param fromDate
     * @param toDate
     * @return
     */
    @LoginRequired
    @RequestMapping(value = "/data/uv", method = RequestMethod.POST)
    public String getUV(Model model,
                        @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
                        @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {
        long uv = dataService.calculateUV(fromDate, toDate);
        model.addAttribute("uv", uv);
        model.addAttribute("uvFromDate", fromDate);
        model.addAttribute("uvToDate", toDate);
        return "forward:/data";//转发给/data
    }

    /**
     * 统计dau，转发给/data
     *
     * @param model
     * @param fromDate
     * @param toDate
     * @return
     */
    @LoginRequired
    @RequestMapping(value = "/data/dau", method = RequestMethod.POST)
    public String getDAU(Model model,
                         @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
                         @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {
        long dau = dataService.calculateDAU(fromDate, toDate);
        model.addAttribute("dau", dau);
        model.addAttribute("dauFromDate", fromDate);
        model.addAttribute("dauToDate", toDate);
        return "forward:/data";//转发给/data
    }
}
