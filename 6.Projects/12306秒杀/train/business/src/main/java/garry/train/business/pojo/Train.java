package garry.train.business.pojo;

import lombok.Data;
import java.util.Date;

/**
 * @author Garry
 * 2024-09-22 13:19
 */
@Data
public class Train {

    /**
     * id
     */
    private Long id;

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