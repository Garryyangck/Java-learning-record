package garry.community.enums;

import lombok.Getter;

/**
 * @author Garry
 * ---------2024/3/21 13:35
 **/
@Getter
public enum CommentStatusEnum {

    NORMAL(0, "正常"),

    BANNED(1, "禁用"),

    ;

    private final Integer code;

    private final String desc;

    CommentStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
