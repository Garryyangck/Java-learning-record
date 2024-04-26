package garry.mall.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Garry
 * ---------2024/3/9 9:35
 **/

/**
 * 商品列表，用到分页，Controller中返回的是PageInfo
 */
@Data
public class ProductVo {
    private Integer id;

    private Integer categoryId;

    private String name;

    private String subtitle;

    private String mainImage;

    private Integer status;

    private BigDecimal price;
}
