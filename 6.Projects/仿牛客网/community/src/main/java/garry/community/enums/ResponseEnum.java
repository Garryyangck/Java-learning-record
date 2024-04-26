package garry.community.enums;

import lombok.Getter;

/**
 * @author Garry
 * ---------2024/3/20 19:41
 **/
@Getter
public enum ResponseEnum {
    ERROR(-1, "服务器异常，操作失败"),

    SUCCESS(0, "操作成功"),

    TO_USER_NOT_EXIST(1, "接收人不存在"),

    NOT_LOGIN(2, "当前用户未登录"),

    LACK_AUTHORITY(3, "你没有进行此操作的权限"),

    FILENAME_NULL(4, "文件名不能为空"),

    CONNECTION_FAILURE(5, "获取数据库连接失败"),
    ;

    private final Integer code;

    private final String msg;

    ResponseEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
