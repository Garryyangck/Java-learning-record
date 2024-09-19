package garry.train.business.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author Garry
 * 2024-09-19 20:52
 */
@Data
public class StationSaveForm {

    /**
     * id
     */
    private Long id;

    /**
     * 站名
     */
    @NotBlank(message = "【站名】不能为空")
    private String name;

    /**
     * 站名拼音
     */
    @NotBlank(message = "【站名拼音】不能为空")
    private String namePinyin;

    /**
     * 站名拼音首字母
     */
    @NotBlank(message = "【站名拼音首字母】不能为空")
    private String namePy;

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
