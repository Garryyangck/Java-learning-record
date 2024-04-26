package garry.community.vo;

import garry.community.enums.ResponseEnum;

/**
 * @author Garry
 * ---------2024/3/20 19:34
 **/

/**
 * 以json字符串形式返回给浏览器的内容，以便浏览器进行异步处理
 *
 * @param <T>
 */
public class ResponseVo<T> {
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

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public static ResponseVo success() {
        return new ResponseVo(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg());
    }

    public static ResponseVo error(ResponseEnum responseEnum) {
        return new ResponseVo(responseEnum.getCode(), responseEnum.getMsg());
    }

    public static <T> ResponseVo<T> success(T data) {
        return new ResponseVo<T>(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg(), data);
    }
}
