package garry.community.advice;

import com.google.gson.Gson;
import garry.community.enums.ResponseEnum;
import garry.community.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Garry
 * ---------2024/3/23 13:55
 **/

/**
 * 统一异常处理，体现aop思想
 */
@Slf4j
@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {

    @ExceptionHandler({Exception.class})
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.error("【服务器发生异常: " + e.getMessage() + "】");
        for (StackTraceElement element : e.getStackTrace()) {
            log.error("【" + element.toString() + "】");
        }
        e.printStackTrace();

        String xRequestedWith = request.getHeader("x-requested-with");
        //ResponseBody类请求，返回服务器错误的提示
        if ("XMLHttpRequest".equals(xRequestedWith)) {
            response.setContentType("application/plain;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(new Gson().toJson(ResponseVo.error(ResponseEnum.ERROR)));
        } else {
            //返回页面类的请求，重定向到"/community/error"
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }
}