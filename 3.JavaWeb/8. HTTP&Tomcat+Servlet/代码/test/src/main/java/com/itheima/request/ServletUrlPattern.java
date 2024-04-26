package com.itheima.request;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

/**
 * 演示urlPattern：一个Servlet可以配置多个urlPattern路径
 *
 * @author Garry
 * ---------2024/2/26 19:26
 **/
@SuppressWarnings({"all"})
@WebServlet(urlPatterns = {"/demo4", "/demo5"})
public class ServletUrlPattern implements Servlet {
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    public ServletConfig getServletConfig() {
        return null;
    }

    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("我可以被/demo4或/demo5访问...");
    }

    public String getServletInfo() {
        return null;
    }

    public void destroy() {

    }
}
