package garry.mall.controller;

import com.github.pagehelper.PageInfo;
import garry.mall.consts.MallConst;
import garry.mall.form.OrderCreateForm;
import garry.mall.pojo.User;
import garry.mall.service.impl.OrderServiceImpl;
import garry.mall.vo.OrderVo;
import garry.mall.vo.ResponseVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author Garry
 * ---------2024/3/12 11:24
 **/
@SuppressWarnings("all")
@RestController
public class OrderController {
    @Resource
    private OrderServiceImpl orderService;

    /**
     * 通过uid和shippingId创建订单（需要登录）
     *
     * @param orderCreateForm
     * @param session
     * @return
     */
    @PostMapping("/orders")
    public ResponseVo<OrderVo> create(@Valid @RequestBody OrderCreateForm orderCreateForm,
                                      HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return orderService.create(user.getId(), orderCreateForm.getShippingId());
    }

    /**
     * 通过uid获取订单列表（需要登录）
     *
     * @param pageNum
     * @param pageSize
     * @param session
     * @return
     */
    @GetMapping("/orders")
    public ResponseVo<PageInfo> list(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                     @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                     HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return orderService.list(user.getId(), pageNum, pageSize);
    }

    /**
     * 获取uid用户的orderNo的订单详情（需要登录）
     *
     * @param orderNo
     * @param session
     * @return
     */
    @GetMapping("/orders/{orderNo}")
    public ResponseVo<OrderVo> detail(@PathVariable(value = "orderNo") Long orderNo,
                                      HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return orderService.detail(user.getId(), orderNo);
    }

    /**
     * 取消订单（需要登录）
     *
     * @param orderNo
     * @param session
     * @return
     */
    @PutMapping("/orders/{orderNo}")
    public ResponseVo cancel(@PathVariable(value = "orderNo") Long orderNo,
                             HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return orderService.cancel(user.getId(), orderNo);
    }
}
