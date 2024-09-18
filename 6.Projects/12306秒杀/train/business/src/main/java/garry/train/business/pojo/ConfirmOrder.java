package garry.train.business.pojo;

import lombok.Data;
import java.util.Date;

/**
* @author Garry
* 2024-09-18 15:52
*/
@Data
public class ConfirmOrder {

    /**
     * id
     */
    private Long id;

    /**
     * 会员id
     */
    private Long memberId;

    /**
     * 日期
     */
    private Date date;

    /**
     * 车次编号
     */
    private String trainCode;

    /**
     * 出发站
     */
    private String start;

    /**
     * 到达站
     */
    private String end;

    /**
     * 余票ID
     */
    private Long dailyTrainTicketId;

    /**
     * 车票
     */
    private String tickets;

    /**
     * 订单状态|枚举[ConfirmOrderStatusEnum]
     */
    private String status;

    /**
     * 新增时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}