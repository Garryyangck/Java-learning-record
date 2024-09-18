package garry.train.business.pojo;

import lombok.Data;
import java.util.Date;

/**
* @author Garry
* 2024-09-18 15:52
*/
@Data
public class TrainSeat {

    /**
     * id
     */
    private Long id;

    /**
     * 车次编号
     */
    private String trainCode;

    /**
     * 厢序
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
     * 同车厢座序
     */
    private Integer carriageSeatIndex;

    /**
     * 新增时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}