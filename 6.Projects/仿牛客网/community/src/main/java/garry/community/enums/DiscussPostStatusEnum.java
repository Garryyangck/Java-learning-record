package garry.community.enums;

import lombok.Getter;

/**
 * @author Garry
 * ---------2024/3/20 20:53
 **/
@Getter
public enum DiscussPostStatusEnum {
    NORMAL(0, "正常"),

    WONDERFUL(1, "精华"),

    BLACKLIST(2, "拉黑");

    private final Integer code;

    private final String desc;

    DiscussPostStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
