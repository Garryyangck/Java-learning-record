package garry.train.business.vo;

import lombok.Data;

/**
 * @author Garry
 * 2024-09-23 15:39
 */
@Data
public class TrainQueryAllVo {

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
     * 终点站
     */
    private String end;

}
