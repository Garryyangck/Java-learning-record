package com.itheima.request;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

/**
 * @author Garry
 * ---------2024/2/26 17:02
 **/

@SuppressWarnings({"all"})
@WebServlet(value = "/demo1", loadOnStartup = 1)
public class ServletDemo1 implements Servlet {
    private ServletConfig config;

    /**
     * web服务器创建实例对象时调用init方法
     *
     * @param servletConfig
     * @throws ServletException
     */
    public void init(ServletConfig servletConfig) throws ServletException {
        this.config = servletConfig;
        System.out.println("init...");
    }

    /**
     * 返回init中的参数ServletConfig servletConfig
     *
     * @return
     */
    public ServletConfig getServletConfig() {
        return config;
    }

    /**
     * 每次Servlet被访问时，都会调用
     *
     * @param servletRequest
     * @param servletResponse
     * @throws ServletException
     * @throws IOException
     */
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("hello servlet!");
    }

    /**
     * 返回作者等注释信息
     *
     * @return
     */
    public String getServletInfo() {
        return "/**\n" +
                " * @author Garry\n" +
                " * ---------2024/2/26 17:02\n" +
                " **/";
    }

    /**
     * Servlet销毁时调用
     * 内存销毁或服务器关闭时销毁Servlet对象
     */
    public void destroy() {
        System.out.println("destroy...");
    }
}
