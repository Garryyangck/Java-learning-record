package garry.community.enums;

import lombok.Getter;

/**
 * @author Garry
 * ---------2024/3/20 20:50
 **/
@Getter
public enum DiscussPostTypeEnum {
    NORMAL(0, "普通"),

    TOP(1, "置顶");

    private final Integer code;

    private final String desc;

    DiscussPostTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
