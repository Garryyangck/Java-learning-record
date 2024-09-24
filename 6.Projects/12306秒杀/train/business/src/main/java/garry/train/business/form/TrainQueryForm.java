package garry.train.business.form;

import garry.train.common.form.PageForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Garry
 * 2024-09-22 13:19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TrainQueryForm extends PageForm {

    /**
     * 车次编号
     */
    private String code;
}
