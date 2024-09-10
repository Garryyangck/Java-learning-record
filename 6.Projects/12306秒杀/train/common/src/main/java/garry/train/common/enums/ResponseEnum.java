package garry.train.common.enums;

import lombok.Getter;

/**
 * @author Garry
 * 2024-09-06 15:53
 */
@Getter
public enum ResponseEnum {
    ERROR(-1, "服务器异常"),

    SUCCESS(0, "操作成功"),

    PARAMETER_INPUT_ERROR(1, "参数输入异常"),

    MOBILE_REGISTER_EXIST(2, "手机号已注册"),

    MESSAGE_CODE_SEND_FAILED(3, "验证码短信发送失败"),

    MOBILE_NOT_EXIST(4, "手机号不存在"),

    CODE_NOT_EXIST(5, "验证码不存在或已过期"),

    WRONG_CODE(6, "验证码不正确"),
    ;

    private final Integer code;

    private final String msg;

    ResponseEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
