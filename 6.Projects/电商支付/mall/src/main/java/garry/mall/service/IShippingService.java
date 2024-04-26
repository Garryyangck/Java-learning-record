package garry.mall.service;

import com.github.pagehelper.PageInfo;
import garry.mall.form.ShippingForm;
import garry.mall.vo.ResponseVo;

import java.util.Map;

/**
 * @author Garry
 * ---------2024/3/10 19:24
 **/
@SuppressWarnings("all")
public interface IShippingService {
    /**
     * 添加收货地址（需要登录）
     *
     * @param shippingForm
     * @return
     */
    ResponseVo<Map<String, Integer>> add(Integer uid, ShippingForm shippingForm);

    /**
     * 删除收货地址（需要登录）
     *
     * @param uid
     * @param shippingId
     * @return
     */
    ResponseVo delete(Integer uid, Integer shippingId);

    /**
     * 更新收获地址（需要登录）
     *
     * @param uid
     * @param shippingId
     * @param shippingForm
     * @return
     */
    ResponseVo update(Integer uid, Integer shippingId, ShippingForm shippingForm);

    /**
     * 查询用户所有收货地址，可分页（需要登录）
     *
     * @param uid
     * @param pageNum
     * @param pageSize
     * @return
     */
    ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize);
}
