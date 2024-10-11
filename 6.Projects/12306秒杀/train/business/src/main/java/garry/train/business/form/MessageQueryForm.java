package garry.train.business.form;

import garry.train.common.form.PageForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Garry
 * 2024-10-11 14:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MessageQueryForm extends PageForm {

    /**
     * 接收者id
     */
    private Long toId;
}
