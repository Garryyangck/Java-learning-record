package garry.mall.controller;

import garry.mall.consts.MallConst;
import garry.mall.form.CartAddForm;
import garry.mall.form.CartUpdateForm;
import garry.mall.pojo.User;
import garry.mall.service.impl.CartServiceImpl;
import garry.mall.vo.CartVo;
import garry.mall.vo.ResponseVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author Garry
 * ---------2024/3/9 19:08
 **/
@SuppressWarnings("all")
@RestController
public class CartController {
    @Resource
    private CartServiceImpl cartService;

    /**
     * 获取购物车列表（需要登录）
     *
     * @param session
     * @return
     */
    @GetMapping("/carts")
    public ResponseVo<CartVo> list(HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.list(user.getId());
    }

    /**
     * 为购物车添加商品（需要登录）
     *
     * @param cartAddForm
     * @return
     */
    @PostMapping("/carts")
    public ResponseVo<CartVo> add(@Valid @RequestBody CartAddForm cartAddForm,
                                  HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.add(user.getId(), cartAddForm);
    }

    /**
     * 更新购物车指定商品（需要登录）
     *
     * @param cartUpdateForm
     * @param productId
     * @param session
     * @return
     */
    @PutMapping("/carts/{productId}")
    public ResponseVo<CartVo> update(@Valid @RequestBody CartUpdateForm cartUpdateForm,
                                     @PathVariable(value = "productId") Integer productId,
                                     HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.update(user.getId(), productId, cartUpdateForm);
    }

    /**
     * 删除购物车的指定商品（需要登录）
     *
     * @param productId
     * @param session
     * @return
     */
    @DeleteMapping("/carts/{productId}")
    public ResponseVo<CartVo> delete(@PathVariable(value = "productId") Integer productId,
                                     HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.delete(user.getId(), productId);
    }

    /**
     * 购物车商品全选（需要登录）
     *
     * @param session
     * @return
     */
    @PutMapping("/carts/selectAll")
    public ResponseVo<CartVo> selectAll(HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.selectAll(user.getId());
    }

    /**
     * 购物车商品全不选（需要登录）
     *
     * @param session
     * @return
     */
    @PutMapping("/carts/unSelectAll")
    public ResponseVo<CartVo> unSelectAll(HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.unSelectAll(user.getId());
    }

    /**
     * 购物车商品总数（需要登录）
     *
     * @param session
     * @return
     */
    @GetMapping("/carts/products/sum")
    public ResponseVo<Integer> sum(HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.sum(user.getId());
    }
}
