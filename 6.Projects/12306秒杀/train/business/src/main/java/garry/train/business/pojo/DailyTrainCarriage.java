package garry.train.business.pojo;

import lombok.Data;
import java.util.Date;

/**
* @author Garry
* 2024-09-18 15:52
*/
@Data
public class DailyTrainCarriage {

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
    private Integer index;

    /**
     * 座位类型|枚举[SeatTypeEnum]
     */
    private String seatType;

    /**
     * 座位数
     */
    private Integer seatCount;

    /**
     * 排数
     */
    private Integer rowCount;

    /**
     * 列数
     */
    private Integer colCount;

    /**
     * 新增时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}