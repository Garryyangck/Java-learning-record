package garry.train.common.vo;

import garry.train.common.enums.ResponseEnum;
import lombok.Data;

/**
 * @author Garry
 * 2024-09-06 15:46
 */
@Data
public class ResponseVo<T> {
    private boolean success = true;

    private Integer code;

    private String msg;

    private T data;

    private ResponseVo(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private ResponseVo(Integer code, String msg) {
        this(code, msg, null);
    }

    private ResponseVo(Integer code, String msg, boolean success) {
        this.code = code;
        this.msg = msg;
        this.success = success;
    }

    public static ResponseVo success() {
        return new ResponseVo(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg());
    }

    public static <T> ResponseVo<T> success(T data) {
        return new ResponseVo<>(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg(), data);
    }

    public static ResponseVo error(ResponseEnum responseEnum) {
        return new ResponseVo(responseEnum.getCode(), responseEnum.getMsg(), false);
    }

    public static ResponseVo error(String errorMsg) {
        return new ResponseVo(ResponseEnum.ERROR.getCode(), errorMsg, false);
    }

    public static ResponseVo error(ResponseEnum responseEnum, String errorMsg) {
        return new ResponseVo(responseEnum.getCode(), errorMsg, false);
    }
}
