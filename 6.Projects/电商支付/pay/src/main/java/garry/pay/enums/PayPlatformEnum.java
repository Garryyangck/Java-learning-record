package garry.pay.enums;

import lombok.Getter;

/**
 * @author Garry
 * ---------2024/3/7 10:34
 **/
@Getter
public enum PayPlatformEnum {
    ALIPAY(1),

    WX(2),
    ;
    Integer code;

    PayPlatformEnum(Integer code) {
        this.code = code;
    }
}
