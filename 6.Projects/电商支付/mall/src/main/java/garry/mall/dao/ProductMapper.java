package garry.mall.dao;

import garry.mall.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@SuppressWarnings("all")
public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    /**
     * 该方法必须手动设置@Param("categoryIdSet")，否则mybatis无法识别传入的集合
     *
     * @param categoryIdSet
     * @return
     */
    List<Product> selectByCategoryIdSet(@Param("categoryIdSet") Set<Integer> categoryIdSet);

    /**
     * 通过idSet查找商品
     *
     * @param idSet
     * @return
     */
    List<Product> selectByIdSet(@Param("idSet") Set<Integer> idSet);
}