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
    /**
     * 已经继承 pageNum、pageSize，在这下面自定义用于过滤查询结果的字段
     */
}
