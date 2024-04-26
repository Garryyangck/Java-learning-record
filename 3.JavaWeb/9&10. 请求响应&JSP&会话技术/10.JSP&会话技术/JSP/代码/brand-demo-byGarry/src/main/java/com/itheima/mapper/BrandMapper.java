package com.itheima.mapper;

import com.itheima.pojo.Brand;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Garry
 * ---------2024/2/28 10:23
 **/
@SuppressWarnings({"all"})
public interface BrandMapper {

    /**
     * 查询所有品牌的数据
     *
     * @return Brand的List集合
     */
    @Select("select * from tb_brand")
    @ResultMap("brandResultMap")
    List<Brand> selectAll();

    /**
     * 插入品牌
     *
     * @param brand
     */
    @Insert("insert into tb_brand values(null,#{brandName},#{companyName},#{ordered},#{description},#{status})")
    @ResultMap("brandResultMap")
    void add(Brand brand);

    /**
     * 根据id查询品牌
     *
     * @param id
     * @return
     */
    @Select("select * from tb_brand where id=#{id}")
    @ResultMap("brandResultMap")
    Brand selectById(int id);

    /**
     * 根据id修改品牌数据
     *
     * @param brand
     */
    @Update("update tb_brand set brand_name=#{brandName},company_name=#{companyName},ordered=#{ordered},description=#{description},status=#{status} where id=#{id}")
    @ResultMap("brandResultMap")
    void update(Brand brand);

    /**
     * 根据id唯一的删除一行
     *
     * @param id
     */
    @Delete("delete from tb_brand where id=#{id}")
    @ResultMap("brandResultMap")
    void delete(int id);
}
