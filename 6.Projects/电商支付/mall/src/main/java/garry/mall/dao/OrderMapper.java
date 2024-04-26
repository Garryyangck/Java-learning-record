package garry.mall.dao;

import garry.mall.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    List<Order> selectByUserId(@Param(value = "userId") Integer userId);

    Order selectByUserIdAndOrderNo(@Param(value = "userId") Integer userId,
                                   @Param(value = "orderNo") Long orderNo);

    Order selectByOrderNo(@Param(value = "orderNo") Long orderNo);
}