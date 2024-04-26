package com.itheima.service;

import com.itheima.mapper.BrandMapper;
import com.itheima.pojo.Brand;
import com.itheima.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * @author Garry
 * ---------2024/2/28 11:09
 * 用于提供实现Brand相关服务的方法
 **/
@SuppressWarnings({"all"})
public class BrandService {
    //只获取一次工厂
    private static final SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    /**
     * 查询所有
     *
     * @return
     */
    public List<Brand> selectAll() {
        SqlSession sqlSession = factory.openSession();
        BrandMapper mapper = sqlSession.getMapper(BrandMapper.class);//反射机制

        List<Brand> brands = mapper.selectAll();

        sqlSession.close();
        return brands;
    }

    /**
     * 添加品牌
     *
     * @param brand
     */
    public void add(Brand brand) {
        SqlSession sqlSession = factory.openSession();
        BrandMapper mapper = sqlSession.getMapper(BrandMapper.class);

        mapper.add(brand);
        sqlSession.commit();//增删改的方法(DML)必须提交事务！

        sqlSession.close();
    }

    /**
     * 根据id查询品牌
     *
     * @param id
     * @return
     */
    public Brand selectById(int id) {
        SqlSession sqlSession = factory.openSession();
        BrandMapper mapper = sqlSession.getMapper(BrandMapper.class);

        Brand brand = mapper.selectById(id);

        sqlSession.close();
        return brand;
    }

    /**
     * 根据id修改品牌数据
     *
     * @param brand
     */
    public void update(Brand brand) {
        SqlSession sqlSession = factory.openSession();
        BrandMapper mapper = sqlSession.getMapper(BrandMapper.class);

        mapper.update(brand);
        sqlSession.commit();//增删改的方法(DML)必须提交事务！

        sqlSession.close();
    }

    /**
     * 根据id唯一的删除一行
     *
     * @param id
     */
    public void delete(int id) {
        SqlSession sqlSession = factory.openSession();
        BrandMapper mapper = sqlSession.getMapper(BrandMapper.class);

        mapper.delete(id);
        sqlSession.commit();//增删改的方法(DML)必须提交事务！

        sqlSession.close();
    }
}
