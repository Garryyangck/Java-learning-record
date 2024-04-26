package garry.mall.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Garry
 * ---------2024/3/12 15:47
 **/
@Data
public class OrderCreateForm {

    @NotNull
    private Integer shippingId;
}
