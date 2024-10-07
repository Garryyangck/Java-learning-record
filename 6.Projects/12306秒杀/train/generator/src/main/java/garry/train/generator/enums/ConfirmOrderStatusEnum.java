package garry.train.generator.enums;

/**
 * @author Garry
 * 2024-10-07 19:59
 */
public enum ConfirmOrderStatusEnum {

    INIT("I","初始"),

    PENDING("P","处理中"),

    SUCCESS("S","成功"),

    FAILURE("F","失败"),

    EMPTY("E","无票"),

    CANCEL("C","取消"),
    ;

    private final String code;

    private final String desc;

    ConfirmOrderStatusEnum(String code, String desc) {
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
