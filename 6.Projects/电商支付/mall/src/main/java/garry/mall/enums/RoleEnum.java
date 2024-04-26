package garry.mall.enums;

import lombok.Getter;

/**
 * @author Garry
 * ---------2024/3/6 19:55
 **/

/**
 * 用户身份枚举类
 */
@Getter
public enum RoleEnum {

    ADMIN(0),

    CUSTOMER(1),
    ;
    Integer code;

    RoleEnum(Integer code) {
        this.code = code;
    }
}
