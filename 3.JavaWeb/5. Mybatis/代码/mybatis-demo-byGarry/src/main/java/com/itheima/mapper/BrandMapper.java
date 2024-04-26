package com.itheima.mapper;

import com.itheima.pojo.Brand;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author Garry
 * ---------2024/2/25 14:08
 **/
public interface BrandMapper {

    /**
     * 查询所有
     *
     * @return
     */
    List<Brand> selectAll();

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    Brand selectById(@Param("id") int id);

    /**
     * 多条件查询，散装参数
     */
    List<Brand> selectByCondition(@Param("status") int status,
                                  @Param("companyName") String companyName,
                                  @Param("brandName") String brandName);

    /**
     * 多条件查询，包装为实例对象
     */
    List<Brand> selectByCondition(Brand brand);

    /**
     * 多条件查询，包装为Map集合，键为String型参数名称
     */
    List<Brand> selectByCondition(Map map);

    /**
     * 单条件查询
     */
    List<Brand> selectByConditionSingle(Brand brand);

    /**
     * 添加商品
     */
    void add(Brand brand);

    /**
     * 修改商品
     */
    int update(Brand brand);

    /**
     * 根据id删除
     */
    void deleteById(int id);

    /**
     * 批量删除
     */
    void deleteByIds(@Param("ids") int[] ids);
}
