package garry.community.enums;

import lombok.Getter;

/**
 * @author Garry
 * ---------2024/3/17 19:57
 **/
@Getter
public enum ActivationEnum {
    ACTIVATION_SUCCESS(0, "激活成功，您的账号已经可以正常使用了"),

    ACTIVATION_REPEAT(1, "该账号已激活，请勿重复激活"),

    ACTIVATION_FAIL(2, "激活失败，请检查您的激活码");

    private final Integer code;

    private final String desc;

    ActivationEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
