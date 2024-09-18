package garry.train.business.pojo;

import lombok.Data;
import java.util.Date;
import java.math.BigDecimal;

/**
 * @author Garry
 * 2024-09-18 16:29
 */
@Data
public class DailyTrainTicket {

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
     * 出发站
     */
    private String start;

    /**
     * 出发站拼音
     */
    private String startPinyin;

    /**
     * 出发时间
     */
    private Date startTime;

    /**
     * 出发站序|本站是整个车次的第几站
     */
    private Integer startIndex;

    /**
     * 到达站
     */
    private String end;

    /**
     * 到达站拼音
     */
    private String endPinyin;

    /**
     * 到站时间
     */
    private Date endTime;

    /**
     * 到站站序|本站是整个车次的第几站
     */
    private Integer endIndex;

    /**
     * 一等座余票
     */
    private Integer ydz;

    /**
     * 一等座票价
     */
    private BigDecimal ydzPrice;

    /**
     * 二等座余票
     */
    private Integer edz;

    /**
     * 二等座票价
     */
    private BigDecimal edzPrice;

    /**
     * 软卧余票
     */
    private Integer rw;

    /**
     * 软卧票价
     */
    private BigDecimal rwPrice;

    /**
     * 硬卧余票
     */
    private Integer yw;

    /**
     * 硬卧票价
     */
    private BigDecimal ywPrice;

    /**
     * 新增时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}