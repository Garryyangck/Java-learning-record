package garry.train.business.pojo;

import lombok.Data;
import java.util.Date;
import java.math.BigDecimal;

/**
 * @author Garry
 * 2024-09-18 16:29
 */
@Data
public class TrainStation {

    /**
     * id
     */
    private Long id;

    /**
     * 车次编号
     */
    private String trainCode;

    /**
     * 站序
     */
    private Integer index;

    /**
     * 站名
     */
    private String name;

    /**
     * 站名拼音
     */
    private String namePinyin;

    /**
     * 进站时间
     */
    private Date inTime;

    /**
     * 出站时间
     */
    private Date outTime;

    /**
     * 停站时长
     */
    private Date stopTime;

    /**
     * 里程（公里）|从上一站到本站的距离
     */
    private BigDecimal km;

    /**
     * 新增时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}