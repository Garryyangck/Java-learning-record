package garry.community.enums;

import lombok.Getter;

/**
 * @author Garry
 * ---------2024/3/17 11:15
 **/

/**
 * 浏览器接收后台返回的消息类型枚举
 */
@Getter
public enum MsgEnum {
    USERNAME_MSG(1, "usernameMsg"),

    PASSWORD_MSG(2, "passwordMsg"),

    EMAIL_MSG(3, "emailMsg"),

    VERIFY_CODE_MSG(4, "verifyCodeMsg"),

    LOGIN_TICKET_MSG(5, "loginTicketMsg"),

    FILE_MSG(6, "fileMsg"),

    NEW_PASSWORD_MSG(7, "newPasswordMsg");

    private final Integer code;//消息编号

    private final String msg;//消息名称

    MsgEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
