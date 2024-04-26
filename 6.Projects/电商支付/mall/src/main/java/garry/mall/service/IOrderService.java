package garry.mall.service;

import com.github.pagehelper.PageInfo;
import garry.mall.vo.OrderVo;
import garry.mall.vo.ResponseVo;

/**
 * @author Garry
 * ---------2024/3/11 10:16
 **/
@SuppressWarnings("all")
public interface IOrderService {

    /**
     * 通过uid和shippingId创建订单（需要登录）
     *
     * @param uid
     * @param shippingId
     * @return
     */
    ResponseVo<OrderVo> create(Integer uid/*通过uid获取购物车信息*/, Integer shippingId);

    /**
     * 通过uid获取订单列表（需要登录）
     *
     * @param uid
     * @param pageNum
     * @param pageSize
     * @return
     */
    ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize);

    /**
     * 获取uid用户的orderNo的订单详情（需要登录）
     *
     * @param uid
     * @param orderNo
     * @return
     */
    ResponseVo<OrderVo> detail(Integer uid, Long orderNo);

    /**
     * 取消订单（需要登录）
     *
     * @param uid
     * @param orderNo
     * @return
     */
    ResponseVo cancel(Integer uid, Long orderNo);

    /**
     * 修改订单状态(NOT_PAY -> PAYED)
     *
     * @param orderNo
     */
    void paid(Long orderNo);
}

