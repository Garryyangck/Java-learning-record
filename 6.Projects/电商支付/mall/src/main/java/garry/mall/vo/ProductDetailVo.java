package garry.mall.vo;

/**
 * @author Garry
 * ---------2024/3/9 16:02
 **/

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品详情
 * 虽然和商品的字段一样，但是还是要单独拎出来，因为之后商品字段可能会有增加
 */
@Data
public class ProductDetailVo {
    private Integer id;

    private Integer categoryId;

    private String name;

    private String subtitle;

    private String mainImage;

    private String subImages;

    private String detail;

    private BigDecimal price;

    private Integer stock;

    private Integer status;

    private Date createTime;

    private Date updateTime;
}
