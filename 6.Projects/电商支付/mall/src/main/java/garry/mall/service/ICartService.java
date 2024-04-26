package garry.mall.service;

import garry.mall.form.CartAddForm;
import garry.mall.form.CartUpdateForm;
import garry.mall.vo.CartVo;
import garry.mall.vo.ResponseVo;

/**
 * @author Garry
 * ---------2024/3/9 20:51
 **/
@SuppressWarnings("all")
public interface ICartService {

    /**
     * 获取uid用户的购物车列表
     *
     * @param uid
     * @return
     */
    ResponseVo<CartVo> list(Integer uid);

    /**
     * 为购物车添加商品
     *
     * @param uid
     * @param cartAddForm
     * @return
     */
    ResponseVo<CartVo> add(Integer uid, CartAddForm cartAddForm);

    /**
     * 更新购物车，更新购物车中指定的商品的数量，以及是否选中
     *
     * @param uid
     * @param productId
     * @param cartUpdateForm
     * @return
     */
    ResponseVo<CartVo> update(Integer uid, Integer productId, CartUpdateForm cartUpdateForm);

    /**
     * 删除购物车的指定商品
     *
     * @param uid
     * @param productId
     * @return
     */
    ResponseVo<CartVo> delete(Integer uid, Integer productId);

    /**
     * 全选中
     *
     * @param uid
     * @return
     */
    ResponseVo<CartVo> selectAll(Integer uid);

    /**
     * 全不选中
     *
     * @param uid
     * @return
     */
    ResponseVo<CartVo> unSelectAll(Integer uid);

    /**
     * 获取购物中所有商品数量总和
     *
     * @param uid
     * @return
     */
    ResponseVo<Integer> sum(Integer uid);
}
