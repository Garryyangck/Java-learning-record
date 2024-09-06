package garry.train.common.controller;

import garry.train.common.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Garry
 * 2024-09-06 17:16
 */

/**
 * 统一异常处理
 */
@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({RuntimeException.class})
    @ResponseBody // 这里必须要加ResponseBody，否则返回的不是JSON字符串！
    public ResponseVo exceptionHandler(RuntimeException e) {
        return ResponseVo.error(e.getMessage());
    }
}
