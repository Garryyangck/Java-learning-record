package garry.train.batch.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author Garry
 * 2024-09-25 15:05
 */
@Data
public class CronVo {

    /**
     * 任务全类名 (用到了反射，必须和全类名相同)
     */
    private String name;

    /**
     * 任务组别
     */
    private String group;

    /**
     * 任务的描述
     */
    private String description;

    /**
     * 任务的 cron 表达式，描述何时执行任务
     */
    private String cronExpression;

    /**
     * 任务的状态: {NONE, NORMAL, PAUSED, COMPLETE, ERROR, BLOCKED }
     */
    private String state;

    /**
     * 下一次计划触发 Trigger 的时间
     * 如果 Trigger 不再触发，则为 null
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date nextFireTime;

    /**
     * 上一次 Trigger 触发的时间
     * 如果 Trigger 还从未触发，则为 null
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date preFireTime;

}
