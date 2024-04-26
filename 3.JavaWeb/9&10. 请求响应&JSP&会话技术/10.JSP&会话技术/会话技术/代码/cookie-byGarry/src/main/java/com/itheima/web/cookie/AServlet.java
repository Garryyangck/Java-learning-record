package com.itheima.web.cookie;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * @author Garry
 * ---------2024/2/28 15:55
 **/
@WebServlet("/aServlet")
public class AServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //解决Cookie不能直接存中文的问题：URL编码和解码
        String value = "张三";
        value = URLEncoder.encode(value, "utf-8");

        //服务器创建Cookie
        Cookie cookie = new Cookie("username", value);

        //设置cookie存活时间，单位为秒，将Cookie存到磁盘中而非内存
        cookie.setMaxAge(60 * 60 * 24 * 7);

        //服务器发送Cookie，客户端接收的Cookie默认存在浏览器分配到的内存里
        response.addCookie(cookie);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
