package garry.mall.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 购物车中每一个商品会生成一个对应的OrderItem属性
 */
@Data
public class OrderItem {
    private Integer id;

    private Integer userId;//uid获取

    private Long orderNo;//来自orderNo订单

    private Integer productId;//cartProductVo

    private String productName;//cartProductVo

    private String productImage;//cartProductVo

    private BigDecimal currentUnitPrice;//生成订单时的商品单价（购物车中显示的单价），cartProductVo

    private Integer quantity;//该商品购买数量，cartProductVo

    private BigDecimal totalPrice;//该商品的总价，cartProductVo

    private Date createTime;

    private Date updateTime;

    public OrderItem() {
    }

    public OrderItem(Integer productId, String productName, String productImage, BigDecimal currentUnitPrice, Integer quantity, BigDecimal totalPrice) {
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.currentUnitPrice = currentUnitPrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
}