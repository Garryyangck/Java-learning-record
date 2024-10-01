package garry.train.member.form;

import garry.train.common.form.PageForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Garry
 * 2024-10-01 17:21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TicketQueryForm extends PageForm {
    /**
     * 会员id，不要求非空，因为会在 Controller 中调用 hostHolder 获取
     */
    private Long memberId;
}
