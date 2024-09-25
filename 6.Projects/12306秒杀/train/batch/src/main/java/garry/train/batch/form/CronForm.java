package garry.train.batch.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Garry
 * 2024-09-25 14:30
 */
@Data
public class CronForm {

    /**
     * 任务全类名 (用到了反射，必须和全类名相同)
     */
    @NotBlank(message = "【任务全类名】不能为空")
    private String name;

    /**
     * 任务组别
     */
    @NotBlank(message = "【任务组别】不能为空")
    private String group;

    /**
     * 任务的描述
     */
    private String description;

    /**
     * 任务的 cron 表达式，描述何时执行任务
     */
    private String cronExpression;

}
