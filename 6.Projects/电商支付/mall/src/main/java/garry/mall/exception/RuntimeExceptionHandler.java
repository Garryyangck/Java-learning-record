package garry.mall.exception;

import garry.mall.enums.ResponseEnum;
import garry.mall.vo.ResponseVo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Garry
 * ---------2024/3/6 22:42
 **/

/**
 * 统一RuntimeException异常处理类
 */
@SuppressWarnings("all")
@ControllerAdvice
@ResponseStatus(HttpStatus.FORBIDDEN/*403*/)
public class RuntimeExceptionHandler {
    /**
     * 其它在运行过程中抛出的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseVo handle(RuntimeException e) {
        return ResponseVo.error(ResponseEnum.ERROR, e.getMessage());
    }

    /**
     * 处理UserLogin异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(UserLoginException.class)
    @ResponseBody
    public ResponseVo handle(UserLoginException e) {
        return ResponseVo.error(ResponseEnum.NEED_LOGIN);
    }

    /**
     * 处理参数不符合要求异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseVo handle(MethodArgumentNotValidException e) {
        return ResponseVo.error(ResponseEnum.PARAM_ERROR, e.getBindingResult());
    }
}
