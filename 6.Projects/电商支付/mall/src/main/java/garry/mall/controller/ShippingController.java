package garry.mall.controller;

import com.github.pagehelper.PageInfo;
import garry.mall.consts.MallConst;
import garry.mall.form.ShippingForm;
import garry.mall.pojo.User;
import garry.mall.service.impl.ShippingServiceImpl;
import garry.mall.vo.ResponseVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;

/**
 * @author Garry
 * ---------2024/3/10 19:32
 **/
@SuppressWarnings("all")
@RestController
public class ShippingController {
    @Resource
    private ShippingServiceImpl shippingService;

    /**
     * 添加收货地址（需要登录）
     *
     * @param shippingForm
     * @param session
     * @return
     */
    @PostMapping("/shippings")
    public ResponseVo<Map<String, Integer>> add(@Valid @RequestBody ShippingForm shippingForm,
                                                HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return shippingService.add(user.getId(), shippingForm);
    }

    /**
     * 删除收货地址（需要登录）
     *
     * @param shippingId
     * @param session
     * @return
     */
    @DeleteMapping("/shippings/{shippingId}")
    public ResponseVo delete(@PathVariable(value = "shippingId") Integer shippingId,
                             HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return shippingService.delete(user.getId(), shippingId);
    }

    /**
     * 更新收获地址（需要登录）
     *
     * @param shippingId
     * @param shippingForm
     * @param session
     * @return
     */
    @PutMapping("/shippings/{shippingId}")
    public ResponseVo update(@PathVariable(value = "shippingId") Integer shippingId,
                             @Valid @RequestBody ShippingForm shippingForm,
                             HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return shippingService.update(user.getId(), shippingId, shippingForm);
    }

    /**
     * 查询用户所有收货地址，可分页（需要登录）
     *
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/shippings")
    public ResponseVo<PageInfo> list(HttpSession session,
                                     @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                     @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return shippingService.list(user.getId(), pageNum, pageSize);
    }
}
