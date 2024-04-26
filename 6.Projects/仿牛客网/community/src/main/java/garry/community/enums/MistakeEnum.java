package garry.community.enums;

import garry.community.consts.CommunityConst;
import lombok.Getter;

/**
 * @author Garry
 * ---------2024/3/17 11:04
 **/

/**
 * 浏览器传入数据的错误类型枚举
 */
@Getter
public enum MistakeEnum {
    USERNAME_BLANK(MsgEnum.USERNAME_MSG.getMsg(), "账号不能为空"),

    PASSWORD_BLANK(MsgEnum.PASSWORD_MSG.getMsg(), "密码不能为空"),

    EMAIL_BLANK(MsgEnum.EMAIL_MSG.getMsg(), "邮箱不能为空"),

    USERNAME_EXIST(MsgEnum.USERNAME_MSG.getMsg(), "该账号已存在"),

    EMAIL_EXIST(MsgEnum.EMAIL_MSG.getMsg(), "该邮箱已被注册"),

    WRONG_EMAIL(MsgEnum.EMAIL_MSG.getMsg(), "邮箱地址有误"),

    PASSWORD_TOO_SHORT(MsgEnum.PASSWORD_MSG.getMsg(), "密码不能低于" + CommunityConst.MIN_PASSWORD_LENGTH + "位"),

    WRONG_USERNAME(MsgEnum.USERNAME_MSG.getMsg(), "该账号不存在"),

    USER_UNACTIVATED(MsgEnum.USERNAME_MSG.getMsg(), "该账号未激活"),

    WRONG_PASSWORD(MsgEnum.PASSWORD_MSG.getMsg(), "密码错误"),

    WRONG_VERIFY_CODE(MsgEnum.VERIFY_CODE_MSG.getMsg(), "验证码错误"),

    FILE_IS_NULL(MsgEnum.FILE_MSG.getMsg(), "您还没有选择文件"),

    WRONG_FILE_PATTERN(MsgEnum.FILE_MSG.getMsg(), "文件的格式不正确"),

    NEW_PASSWORD_BLANK(MsgEnum.NEW_PASSWORD_MSG.getMsg(), "密码不能为空"),

    NEW_PASSWORD_TOO_SHORT(MsgEnum.NEW_PASSWORD_MSG.getMsg(), "密码不能低于" + CommunityConst.MIN_PASSWORD_LENGTH + "位"),

    VERIFY_CODE_BLANK(MsgEnum.VERIFY_CODE_MSG.getMsg(), "验证码为空"),

    VERIFY_CODE_EXPIRED(MsgEnum.VERIFY_CODE_MSG.getMsg(), "验证码已过期，请重新获取"),

    ;

    private final String type;//回复类型

    private final String desc;//回复详情

    MistakeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
