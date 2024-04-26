package garry.mall.enums;

import lombok.Getter;

/**
 * @author Garry
 * ---------2024/3/11 13:06
 **/
@Getter
public enum PaymentTypeEnum {
    ONLINE_PAYMENT(1, "在线支付"),
    ;

    private Integer type;
    private String desc;

    PaymentTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
