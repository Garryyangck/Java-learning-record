package com.itheima.web;

import com.itheima.mapper.BrandMapper;
import com.itheima.pojo.Brand;
import com.itheima.service.BrandService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Garry
 * ---------2024/2/28 12:37
 **/
@WebServlet("/addServlet")
public class AddServlet extends HttpServlet {
    private final BrandService brandService = new BrandService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //处理POST请求中文乱码问题
        request.setCharacterEncoding("utf-8");

        //获取添加的相关信息
        String brandName = request.getParameter("brandName");
        String companyName = request.getParameter("companyName");
        Integer ordered = Integer.parseInt(request.getParameter("ordered"));
        String description = request.getParameter("description");
        Integer status = Integer.parseInt(request.getParameter("status"));

        //创建新增的Brand对象
        Brand brand = new Brand();
        brand.setBrandName(brandName);
        brand.setCompanyName(companyName);
        brand.setOrdered(ordered);
        brand.setDescription(description);
        brand.setStatus(status);

        //添加
        brandService.add(brand);

        //转发到SelectAllServlet
        request.getRequestDispatcher("/selectAllServlet").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
