package garry.mall.enums;

import lombok.Getter;

/**
 * @author Garry
 * ---------2024/3/9 16:11
 **/

/**
 * 商品状态枚举
 */
@Getter
public enum ProductStatusEnum {
    ON_SALE(1),

    OFF_SALE(2),

    DELETE(3);

    Integer status;

    ProductStatusEnum(Integer status) {
        this.status = status;
    }
}
