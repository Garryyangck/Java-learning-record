package garry.mall.enums;

import lombok.Getter;

/**
 * @author Garry
 * ---------2024/3/6 21:19
 **/

/**
 * Controller返回的status和msg枚举类
 */
@Getter
public enum ResponseEnum {

    ERROR(-1, "服务器异常"),

    SUCCESS(0, "成功"),

    PARAM_ERROR(1, "参数错误"),

    USERNAME_EXIST(2, "用户已存在"),

    EMAIL_EXIST(3, "邮箱已存在"),

    PASSWORD_ERROR(4, "密码错误"),

    USER_NOT_FOUND(5, "用户名不存在"),

    PRODUCT_OFF_SALE_OR_DELETE(6, "商品不存在、已下架或被删除"),

    LACK_OF_PRODUCT(7, "商品库存不足"),

    PRODUCT_NOT_EXIST(8, "购物车没有此商品"),

    SHIPPING_NOT_EXIST(9, "收货地址不存在"),

    NEED_LOGIN(10, "用户未登录，请先登录"),

    CART_EMPTY(11, "购物车没有选中的商品"),

    ORDER_CREATE_FAIL(12, "订单创建失败"),

    ORDER_NOT_FOUND(13, "没有找到订单"),

    ORDER_IS_PAYED(14, "订单已付款，无法被取消"),

    ORDER_CANCEL_FAIL(15, "订单取消失败"),//服务端故障导致

    ORDER_STATUS_ERROR(16, "订单状态异常"),

    ORDER_PAID_FAIL(17, "订单状态更新为支付失败");

    Integer status;
    String msg;

    ResponseEnum(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }
}
