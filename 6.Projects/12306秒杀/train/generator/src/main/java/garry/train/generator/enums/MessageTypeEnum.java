package garry.train.generator.enums;

/**
 * @author Garry
 * 2024-10-11 14:09
 */
public enum MessageTypeEnum {

    SYSTEM_MESSAGE("0", "系统消息"),

    MEMBER_MESSAGE("1", "用户消息"),
    ;

    private final String code;

    private final String desc;

    MessageTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
