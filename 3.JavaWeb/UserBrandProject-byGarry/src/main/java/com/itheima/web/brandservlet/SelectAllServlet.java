package com.itheima.web.brandservlet;

import com.itheima.pojo.Brand;
import com.itheima.pojo.User;
import com.itheima.service.BrandService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * @author Garry
 * ---------2024/2/28 12:01
 **/
@SuppressWarnings({"all"})
@WebServlet("/selectAllServlet")
public class SelectAllServlet extends HttpServlet {
    private final BrandService brandService = new BrandService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取brands
        List<Brand> brands = brandService.selectAll();

        //存入域
        request.setAttribute("brands", brands);

        //将brands转发给brand.jsp，显示给用户
        request.getRequestDispatcher("/brand.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
