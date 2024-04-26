package garry.community.enums;

import lombok.Getter;

/**
 * @author Garry
 * ---------2024/3/22 15:46
 **/
@Getter
public enum MessageStatusEnum {
    UNREAD(0, "未读"),

    READ(1, "已读"),

    DELETE(2, "删除");

    private final Integer code;

    private final String desc;

    MessageStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
