package garry.train.business.form;

import garry.train.common.form.PageForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Garry
 * 2024-09-19 20:52
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StationQueryForm extends PageForm {
    /**
     * 已经继承 pageNum、pageSize，在这下面自定义用于过滤查询结果的字段
     */
}
