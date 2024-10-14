package garry.train.generator.enums;

/**
 * @author Garry
 * 2024-10-11 14:09
 */
public enum MessageStatusEnum {

    UNREAD("0", "未读"),

    READ("1", "已读"),

    DELETE("2", "删除"),

    TOP("3", "置顶"),

    ;

    private final String code;

    private final String desc;

    MessageStatusEnum(String code, String desc) {
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
