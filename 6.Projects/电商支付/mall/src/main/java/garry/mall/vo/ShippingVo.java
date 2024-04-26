package garry.mall.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author Garry
 * ---------2024/3/10 19:22
 **/
@Data
public class ShippingVo {
    private Integer id;//在订单detail中不显示

    private Integer userId;//需要登录，在订单detail中不显示

    private String receiverName;

    private String receiverPhone;

    private String receiverMobile;

    private String receiverProvince;

    private String receiverCity;

    private String receiverDistrict;

    private String receiverAddress;

    private String receiverZip;

    private Date createTime;//在订单detail中不显示

    private Date updateTime;//在订单detail中不显示
}
