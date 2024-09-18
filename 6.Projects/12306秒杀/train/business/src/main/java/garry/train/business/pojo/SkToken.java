package garry.train.business.pojo;

import lombok.Data;
import java.util.Date;

/**
* @author Garry
* 2024-09-18 15:52
*/
@Data
public class SkToken {

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
     * 令牌余量
     */
    private Integer count;

    /**
     * 新增时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}