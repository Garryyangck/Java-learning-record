package garry.community.enums;

import lombok.Getter;

/**
 * @author Garry
 * ---------2024/3/17 12:26
 **/
@Getter
public enum UserStatusEnum {
    UNACTIVATED(0, "未激活"),

    ACTIVATED(1, "已激活");

    private final Integer code;

    private final String desc;

    UserStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
