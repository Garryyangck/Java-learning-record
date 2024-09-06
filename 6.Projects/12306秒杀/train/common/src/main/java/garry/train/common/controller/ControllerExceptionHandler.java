package garry.train.common.controller;

import garry.train.common.enums.ResponseEnum;
import garry.train.common.exception.BusinessException;
import garry.train.common.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
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

    @ExceptionHandler({Exception.class})
    @ResponseBody // 这里必须要加ResponseBody，否则返回的不是JSON字符串！
    public ResponseVo exceptionHandler(Exception e) {
        log.error("系统异常: " + e);
        return ResponseVo.error(ResponseEnum.ERROR);
    }

    @ExceptionHandler({BusinessException.class})
    @ResponseBody
    public ResponseVo businessExceptionHandler(BusinessException e) {
        log.error("业务异常: {}", e.getResponseEnum().getMsg());
        return ResponseVo.error(e.getResponseEnum());
    }

    @ExceptionHandler({BindException.class})
    @ResponseBody
    public ResponseVo bindExceptionHandler(BindException e) {
        log.error("校验异常: " + e);
        return ResponseVo.error(ResponseEnum.PARAMETER_INPUT_ERROR, e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }
}
