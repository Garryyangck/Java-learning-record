package com.itheima.request;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Garry
 * ---------2024/2/26 19:04
 **/

@SuppressWarnings({"all"})
public class MyHTTPServlet implements Servlet {
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    public ServletConfig getServletConfig() {
        return null;
    }

    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String method = request.getMethod();
        if ("GET".equals(method)) {
            doGet(servletRequest, servletResponse);
        } else if ("POST".equals(method)) {
            doPost(servletRequest, servletResponse);
        }
    }

    /**
     * 子类实现Get的业务逻辑
     *
     * @param servletRequest
     * @param servletResponse
     */
    protected void doGet(ServletRequest servletRequest, ServletResponse servletResponse) {
    }

    /**
     * 子类实现Post的业务逻辑
     *
     * @param servletRequest
     * @param servletResponse
     */
    protected void doPost(ServletRequest servletRequest, ServletResponse servletResponse) {
    }

    public String getServletInfo() {
        return null;
    }

    public void destroy() {

    }
}
