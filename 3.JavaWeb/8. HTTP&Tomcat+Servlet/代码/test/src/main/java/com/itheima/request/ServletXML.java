package com.itheima.request;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author Garry
 * ---------2024/2/26 19:44
 **/
public class ServletXML implements Servlet {

    public void init(ServletConfig servletConfig) throws ServletException {

    }

    public ServletConfig getServletConfig() {
        return null;
    }

    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("ServletXML的service...");
    }

    public String getServletInfo() {
        return null;
    }

    public void destroy() {

    }
}
