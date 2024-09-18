package garry.train.business.pojo;

import lombok.Data;
import java.util.Date;

/**
 * @author Garry
 * 2024-09-18 16:29
 */
@Data
public class DailyTrainSeat {

    /**
     * id
     */
    private Long id;

    /**
     * 日期
     */
    private Date date;

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
    private String row;

    /**
     * 列号|枚举[SeatColEnum]
     */
    private String col;

    /**
     * 座位类型|枚举[SeatTypeEnum]
     */
    private String seatType;

    /**
     * 同车箱座序
     */
    private Integer carriageSeatIndex;

    /**
     * 售卖情况|将经过的车站用01拼接，0表示可卖，1表示已卖
     */
    private String sell;

    /**
     * 新增时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}