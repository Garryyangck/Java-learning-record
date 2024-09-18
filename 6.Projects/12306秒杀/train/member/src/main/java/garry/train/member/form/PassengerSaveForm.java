package garry.train.member.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

/**
* @author Garry
* 2024-09-18 11:51
*/
@Data
public class PassengerSaveForm {

    /**
     * id
     */
    private Long id;

    /**
     * 会员id，不要求非空，因为会在 Controller 中调用 hostHolder 获取
     */
    private Long memberId;

    /**
     * 姓名
     */
    @NotBlank(message = "【姓名】不能为空")
    private String name;

    /**
     * 身份证
     */
    @NotBlank(message = "【身份证】不能为空")
    private String idCard;

    /**
     * 旅客类型|枚举[PassengerTypeEnum]
     */
    @NotBlank(message = "【旅客类型】不能为空")
    private String type;

    /**
     * 新增时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

}
