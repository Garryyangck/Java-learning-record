package com.itheima.response;

import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Garry
 * ---------2024/2/27 15:06
 **/
@WebServlet("/respOutputStream")
public class ResponseOutputStream extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //自己复制流对象，使用工具类org.apache.commons.io.IOUtils
        FileInputStream fis = new FileInputStream("D:\\杨宸楷\\学习\\java-learning\\JavaWeb\\8. HTTP&Tomcat+Servlet\\讲义\\assets\\20210717183958532.png");
        ServletOutputStream os = response.getOutputStream();
        System.out.println(os.getClass());
        IOUtils.copy(fis, os);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
