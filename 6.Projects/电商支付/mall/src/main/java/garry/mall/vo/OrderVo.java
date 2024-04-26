package garry.mall.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Garry
 * ---------2024/3/11 9:41
 **/
@Data
public class OrderVo {//除了写有注释的属性外，其它的属性都可以从Order中拷贝

    private Long orderNo;

    private BigDecimal payment;

    private Integer paymentType;

    private String paymentTypeDesc;//订单detail中才用返回

    private Integer postage;

    private Integer status;

    private String statusDesc;//订单detail中才用返回

    private Date paymentTime;

    private Date sendTime;

    private Date endTime;

    private Date closeTime;

    private Date createTime;//自己设置当前时间

    List<OrderItemVo> orderItemVoList = new ArrayList<>();//一个Order中包含多个商品的OrderItem

    private Integer shippingId;

    private String receiverName;//单独取出来

    private ShippingVo shippingVo;//根据shippingId查出来
}
