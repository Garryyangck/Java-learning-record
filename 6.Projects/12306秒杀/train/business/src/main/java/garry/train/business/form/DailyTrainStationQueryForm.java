package garry.train.business.form;

import garry.train.common.form.PageForm;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Garry
 * 2024-09-28 19:32
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DailyTrainStationQueryForm extends PageForm {

    /**
     * 车次编号
     */
    private String trainCode;

    /**
     * 日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
}
