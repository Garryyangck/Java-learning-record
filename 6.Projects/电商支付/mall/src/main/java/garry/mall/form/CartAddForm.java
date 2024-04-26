package garry.mall.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Garry
 * ---------2024/3/9 19:04
 **/

/**
 * 添加商品
 */
@Data
public class CartAddForm {

    @NotNull
    private Integer productId;

    private boolean selected = true;//默认为true
}
