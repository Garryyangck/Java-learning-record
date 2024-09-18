package garry.train.business.pojo;

import lombok.Data;
import java.util.Date;

/**
* @author Garry
* 2024-09-18 15:52
*/
@Data
public class DailyTrain {

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
    private String code;

    /**
     * 车次类型|枚举[TrainTypeEnum]
     */
    private String type;

    /**
     * 始发站
     */
    private String start;

    /**
     * 始发站拼音
     */
    private String startPinyin;

    /**
     * 出发时间
     */
    private Date startTime;

    /**
     * 终点站
     */
    private String end;

    /**
     * 终点站拼音
     */
    private String endPinyin;

    /**
     * 到站时间
     */
    private Date endTime;

    /**
     * 新增时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}