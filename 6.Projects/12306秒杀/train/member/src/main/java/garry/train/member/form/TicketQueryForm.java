package garry.train.member.form;

import garry.train.common.form.PageForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Garry
 * 2024-10-14 15:19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TicketQueryForm extends PageForm {

    /**
     * 会员id
     */
    private Long memberId;
}
