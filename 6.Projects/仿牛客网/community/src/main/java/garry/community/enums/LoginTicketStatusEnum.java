package garry.community.enums;

import lombok.Getter;

/**
 * @author Garry
 * ---------2024/3/21 13:35
 **/
@Getter
public enum LoginTicketStatusEnum {

    EFFECTIVE(0, "有效"),

    INEFFECTIVE(1, "无效"),

    ;

    private final Integer code;

    private final String desc;

    LoginTicketStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
