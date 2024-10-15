package garry.train.common.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Garry
 * 2024-10-14 21:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApiDetailQueryForm extends PageForm {

    /**
     * 类型 | GET, POST...
     */
    private String apiMethod;

    /**
     * 模块
     */
    private String moduleName;

    /**
     * 排序的字段
     */
    private String sortBy;

    /**
     * 是否升序排序
     */
    private Boolean isAsc;

}
