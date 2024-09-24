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

    MEMBER_MOBILE_REGISTER_EXIST(2, "手机号已注册"),

    MEMBER_MESSAGE_CODE_SEND_FAILED(3, "短信验证码发送失败"),

    MEMBER_MOBILE_NOT_EXIST(4, "手机号输入有误"),

    MEMBER_CODE_NOT_EXIST(5, "验证码不存在或已过期"),

    MEMBER_WRONG_CODE(6, "验证码不正确"),

    MEMBER_WRONG_TOKEN(7, "JWT不存在或已过期"),

    MEMBER_THREAD_LOCAL_ERROR(8, "获取会员ID失败"),

    BUSINESS_WRONG_TRAIN_CODE(9, "车次编号错误或不存在"),

    DUPLICATE_KEY(10, "数据库唯一键异常"),

    BUSINESS_DUPLICATE_STATION_NAME(11, "站名已存在"),

    BUSINESS_DUPLICATE_TRAIN_STATION_INDEX(12, "站序已存在"),

    BUSINESS_DUPLICATE_TRAIN_STATION_NAME(13, "站名已存在"),

    BUSINESS_DUPLICATE_TRAIN_CARRIAGE_INDEX(14, "厢号已存在"),
    ;

    private final Integer code;

    private final String msg;

    ResponseEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
