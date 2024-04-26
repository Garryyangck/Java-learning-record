package com.itheima.request;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;

/**
 * @author Garry
 * ---------2024/2/26 19:09
 **/

@SuppressWarnings({"all"})
@WebServlet(value = "/demo3")
public class ServletDemo3 extends MyHTTPServlet {
    @Override
    protected void doGet(ServletRequest servletRequest, ServletResponse servletResponse) {
        System.out.println("子类get...");
    }

    @Override
    protected void doPost(ServletRequest servletRequest, ServletResponse servletResponse) {
        System.out.println("子类post...");
    }
}
