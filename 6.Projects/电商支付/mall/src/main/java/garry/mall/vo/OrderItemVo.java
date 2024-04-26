package garry.mall.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Garry
 * ---------2024/3/11 9:42
 **/
@Data
public class OrderItemVo {//所有属性都可以可以从OrderItem拷贝

    private Long orderNo;

    private Integer productId;

    private String productName;

    private String productImage;

    private BigDecimal currentUnitPrice;

    private Integer quantity;

    private BigDecimal totalPrice;

    private Date createTime;//自己设置当前时间
}
