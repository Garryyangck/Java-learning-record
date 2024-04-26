package garry.mall.enums;

import lombok.Getter;

/**
 * @author Garry
 * ---------2024/3/11 10:21
 **/
@Getter
public enum OrderStatusEnum {
    CANCELED(0, "已取消"),

    NOT_PAY(10, "未付款"),

    PAYED(20, "已付款"),

    SHIPPED(40, "已发货"),

    DEAL_DONE(50, "交易成功"),

    DEAL_CLOSE(60, "交易关闭"),
    ;

    private Integer status;
    private String desc;

    OrderStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
