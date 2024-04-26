package garry.community.enums;

import lombok.Getter;

/**
 * @author Garry
 * ---------2024/3/17 12:22
 **/
@Getter
public enum UserTypeEnum {
    USER(0, "user"),

    ADMIN(1, "admin"),

    MODERATOR(2, "moderator");

    private final Integer code;

    private final String desc;

    UserTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
