package garry.mall.dao;

import garry.mall.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    int deleteByIdAndUserId(@Param(value = "id") Integer id, @Param(value = "userId") Integer userId);

    List<Shipping> selectByUserId(@Param(value = "userId") Integer userId);

    Shipping selectByIdAndUserId(@Param(value = "id") Integer id, @Param(value = "userId") Integer userId);

    /**
     * 通过IdSet获取Shipping，避免for循环内部多次使用sql
     *
     * @param shippingIdSet
     * @return
     */
    List<Shipping> selectByIdSet(@Param(value = "idSet") Set<Integer> shippingIdSet);
}