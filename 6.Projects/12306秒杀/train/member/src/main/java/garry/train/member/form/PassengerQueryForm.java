package garry.train.member.form;

import garry.train.common.form.PageForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* @author Garry
* 2024-09-18 11:35
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class PassengerQueryForm extends PageForm {
    /**
     * 会员id
     */
    private Long memberId;
}
