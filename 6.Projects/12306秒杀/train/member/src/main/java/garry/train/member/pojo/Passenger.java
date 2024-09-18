package garry.train.member.pojo;

import lombok.Data;
import java.util.Date;

/**
* @author Garry
* 2024-09-18 15:50
*/
@Data
public class Passenger {

    /**
     * id
     */
    private Long id;

    /**
     * 会员id
     */
    private Long memberId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证
     */
    private String idCard;

    /**
     * 旅客类型|枚举[PassengerTypeEnum]
     */
    private String type;

    /**
     * 新增时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}