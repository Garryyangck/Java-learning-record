package com.itheima.web.userservlet;

import com.itheima.pojo.User;
import com.itheima.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author Garry
 * ---------2024/2/28 21:05
 **/
@WebServlet("/registerServlet")
public class RegisterServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String checkCode = request.getParameter("checkCode");

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        boolean flag = userService.register(user);

        if (flag) {
            HttpSession session = request.getSession();
            String check = (String) session.getAttribute("check");
            if (check.equals(checkCode)) {
                request.setAttribute("register_msg", "注册成功，请登录");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            } else {
                request.setAttribute("register_msg", "验证码不正确");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("register_msg", "用户名已存在");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
