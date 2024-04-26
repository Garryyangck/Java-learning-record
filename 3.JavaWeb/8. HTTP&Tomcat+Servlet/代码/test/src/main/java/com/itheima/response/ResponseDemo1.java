package com.itheima.response;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Garry
 * ---------2024/2/27 14:21
 **/
@WebServlet("/resp1")
public class ResponseDemo1 extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("resp1...");

//        //1. 设置响应状态码
//        response.setStatus(302);
//        //2. 设置响应头Location，注意要加虚拟目录/test-demo
//        response.setHeader("Location", "/test-demo/resp2");

        //简化方法
//        response.sendRedirect("https://www.baidu.com");//可以重定向到任意资源

        //问题：sendRedirect里面写死了，如果pom.xml里配置了Tomcat的默认访问路径
        //那么全部写死的地方都必须修改，解决方法：
        String contextPath = request.getContextPath();
        response.sendRedirect(contextPath + "/resp2");

        //发送两次请求到服务器，标签页名改变
        //可以重定向到任意资源(包括外部资源)
        //两个资源不共享信息
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
