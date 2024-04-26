package garry.mall.dao;

import garry.mall.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);

    /**
     * 通过orderItemList写入数据库，避免多次使用sql
     */
    int batchInsert(@Param(value = "orderItemList") List<OrderItem> orderItemList);

    List<OrderItem> selectByUserIdAndOrderNo(@Param(value = "userId") Integer userId,
                                             @Param(value = "orderNo") Long orderNo);

    int deleteByUserIdAndOrderNo(@Param(value = "userId") Integer userId,
                                 @Param(value = "orderNo") Long orderNo);

    /**
     * 通过orderNoSet获取OrderItem，避免for循环内部多次使用sql
     *
     * @param orderNoSet
     * @return
     */
    List<OrderItem> selectByOrderNoSet(@Param(value = "orderNoSet") Set<Long> orderNoSet);
}