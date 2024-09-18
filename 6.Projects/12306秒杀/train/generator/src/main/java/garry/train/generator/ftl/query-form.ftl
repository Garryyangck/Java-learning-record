package garry.train.${module}.form;

import garry.train.common.form.PageForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* @author Garry
* ${DateTime}
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class ${Domain}QueryForm extends PageForm {
    private Long memberId;
}
