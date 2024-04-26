package garry.mall.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Garry
 * ---------2024/3/9 18:52
 **/

/**
 * 购物车中的商品
 */
@Data
public class CartProductVo {

    private Integer productId;

    private Integer quantity;//购买数量

    private String productName;

    private String productSubtitle;

    private String productMainImage;

    private BigDecimal productPrice;

    private Integer productStatus;

    private BigDecimal productTotalPrice;//总价

    private Integer productStock;

    private boolean productSelected;//是否被选中

    public CartProductVo() {
    }

    public CartProductVo(Integer productId, Integer quantity, String productName, String productSubtitle, String productMainImage, BigDecimal productPrice, Integer productStatus, BigDecimal productTotalPrice, Integer productStock, boolean productSelected) {
        this.productId = productId;
        this.quantity = quantity;
        this.productName = productName;
        this.productSubtitle = productSubtitle;
        this.productMainImage = productMainImage;
        this.productPrice = productPrice;
        this.productStatus = productStatus;
        this.productTotalPrice = productTotalPrice;
        this.productStock = productStock;
        this.productSelected = productSelected;
    }
}
