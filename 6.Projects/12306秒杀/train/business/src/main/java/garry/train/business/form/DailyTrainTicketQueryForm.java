package garry.train.business.form;

import garry.train.common.form.PageForm;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Garry
 * 2024-09-30 14:51
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DailyTrainTicketQueryForm extends PageForm {

    /**
     * 车次编号
     */
    private String code;

    /**
     * 日期
     */
    // POST 请求加: @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    // GET 请求加: @DateTimeFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
}
