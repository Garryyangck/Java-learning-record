package garry.train.member.pojo;

import lombok.Data;
import java.util.Date;

/**
 * @author Garry
 * 2024-09-18 16:17
 */
@Data
public class Ticket {

    /**
     * id
     */
    private Long id;

    /**
     * 会员id
     */
    private Long memberId;

    /**
     * 乘客id
     */
    private Long passengerId;

    /**
     * 乘客姓名
     */
    private String passengerName;

    /**
     * 日期
     */
    private Date trainDate;

    /**
     * 车次编号
     */
    private String trainCode;

    /**
     * 箱序
     */
    private Integer carriageIndex;

    /**
     * 排号|01, 02
     */
    private String seatRow;

    /**
     * 列号|枚举[SeatColEnum]
     */
    private String seatCol;

    /**
     * 出发站
     */
    private String startStation;

    /**
     * 出发时间
     */
    private Date startTime;

    /**
     * 到达站
     */
    private String endStation;

    /**
     * 到站时间
     */
    private Date endTime;

    /**
     * 座位类型|枚举[SeatTypeEnum]
     */
    private String seatType;

    /**
     * 新增时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}