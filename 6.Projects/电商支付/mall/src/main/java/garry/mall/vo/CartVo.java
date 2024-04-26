package garry.mall.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Garry
 * ---------2024/3/9 18:50
 **/

/**
 * 购物车
 */
@Data
public class CartVo {

    private List<CartProductVo> cartProductVoList;

    private boolean selectAll = true;

    //非基本数据类型默认初始化为null，此处需要显示初始化为0
    private BigDecimal cartTotalPrice = BigDecimal.valueOf(0);

    //非基本数据类型默认初始化为null，此处需要显示初始化为0
    private Integer cartTotalQuantity = 0;
}
