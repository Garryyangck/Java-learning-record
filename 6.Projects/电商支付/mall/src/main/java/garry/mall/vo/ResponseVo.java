package garry.mall.vo;

/**
 * @author Garry
 * ---------2024/3/6 21:03
 **/

import garry.mall.enums.ResponseEnum;
import lombok.Data;
import org.springframework.validation.BindingResult;

/**
 * Controller返回给前端的对象
 */
@SuppressWarnings("all")
@Data
public class ResponseVo<T> {

    private Integer status;

    private String msg;

    private T/*用户、商品等模块返回的数据不同，故使用泛型*/ data;

    public ResponseVo() {
    }

    public ResponseVo(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public ResponseVo(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 成功后不写入data对象
     *
     * @param <T>
     * @return
     */
    public static <T> ResponseVo<T> success() {
        return new ResponseVo<>(ResponseEnum.SUCCESS.getStatus(), ResponseEnum.SUCCESS.getMsg());
    }

    /**
     * 成功后写入data对象
     *
     * @param user
     * @param <T>
     * @return
     */
    public static <T> ResponseVo<T> success(T data) {
        return new ResponseVo<T>(ResponseEnum.SUCCESS.getStatus(), ResponseEnum.SUCCESS.getMsg(), data);
    }

    /**
     * 传入错误类型，生成对应对象
     *
     * @param responseEnum
     * @param <T>
     * @return
     */
    public static <T> ResponseVo<T> error(ResponseEnum responseEnum) {
        return new ResponseVo<>(responseEnum.getStatus(), responseEnum.getMsg());
    }

    /**
     * 手动写入错误信息
     *
     * @param responseEnum
     * @param <T>
     * @return
     */
    public static <T> ResponseVo<T> error(ResponseEnum responseEnum, String msg) {
        return new ResponseVo<>(responseEnum.getStatus(), msg);
    }

    /**
     * 根据错误类型和表单检验结果返回ResponseVo对象
     *
     * @param responseEnum
     * @param <T>
     * @return
     */
    public static <T> ResponseVo<T> error(ResponseEnum responseEnum, BindingResult bindingResult) {
        return new ResponseVo<>(responseEnum.getStatus(), bindingResult.getFieldError().getField() + " " + bindingResult.getFieldError().getDefaultMessage());
    }
}
