package garry.train.business.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

/**
 * @author Garry
 * 2024-09-22 13:19
 */
@Data
public class TrainSaveForm {

    /**
     * id
     */
    private Long id;

    /**
     * 车次编号|可以为空，在service层中生成车次编号，格式为“type.code+该类型数据库中的数目+三位UUID”
     */
    private String code;

    /**
     * 车次类型|枚举[TrainTypeEnum]
     */
    @NotBlank(message = "【车次类型】不能为空")
    private String type;

    /**
     * 始发站
     */
    @NotBlank(message = "【始发站】不能为空")
    private String start;

    /**
     * 始发站拼音
     */
    @NotBlank(message = "【始发站拼音】不能为空")
    private String startPinyin;

    /**
     * 出发时间
     */
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    @NotNull(message = "【出发时间】不能为空")
    private Date startTime;

    /**
     * 终点站
     */
    @NotBlank(message = "【终点站】不能为空")
    private String end;

    /**
     * 终点站拼音
     */
    @NotBlank(message = "【终点站拼音】不能为空")
    private String endPinyin;

    /**
     * 到站时间
     */
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    @NotNull(message = "【到站时间】不能为空")
    private Date endTime;

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
