package garry.train.member.form;

import garry.train.common.form.PageForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Garry
 * 2024-09-14 21:47
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PassengerQueryForm extends PageForm {
    private Long memberId;
}
