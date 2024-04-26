package garry.mall.pojo;

import lombok.Data;

/**
 * @author Garry
 * ---------2024/3/9 21:46
 **/

/**
 * 购物车
 */
@Data
public class Cart {
    private Integer productId;

    private Integer quantity;//购买数量

    private boolean productSelected;//是否被选中

    public Cart() {
    }

    public Cart(Integer productId, Integer quantity, boolean productSelected) {
        this.productId = productId;
        this.quantity = quantity;
        this.productSelected = productSelected;
    }
}
