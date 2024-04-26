package com.itheima.request;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author Garry
 * ---------2024/2/27 7:59
 **/
@SuppressWarnings({"all"})
@WebServlet(value = "/pr")//忘记加/了...
public class ServletPrintRequest extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest requset, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("get...");
//        String queryString = requset.getQueryString();//username=%E5%BC%A0%E4%B8%89&password=123&hobby=swim&hobby=climb
        Map<String, String[]> parameterMap = requset.getParameterMap();
        for (String key : parameterMap.keySet()) {
            System.out.print(key + ": ");
            String[] contents = parameterMap.get(key);
            for (String content : contents) {
                byte[] bytes = content.getBytes(StandardCharsets.ISO_8859_1);//转为字节数组
                String str = new String(bytes, StandardCharsets.UTF_8);//采用utf-8编码为字符串
                System.out.print(str + " ");
            }
            System.out.println();
        }
    }

    @Override
    protected void doPost(HttpServletRequest requset, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("post...");
        requset.setCharacterEncoding("utf-8");//解决中文乱码，Post底层使用字符流读取，设置解码方式为utf-8
        Map<String, String[]> parameterMap = requset.getParameterMap();
        for (String key : parameterMap.keySet()) {
            System.out.print(key + ": ");
            String[] contents = parameterMap.get(key);
            for (String content : contents) {
                System.out.print(content + " ");
            }
            System.out.println();
        }
    }
}
