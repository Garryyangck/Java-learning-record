package garry.train.batch.controller;

import garry.train.batch.form.CronForm;
import garry.train.batch.vo.CronVo;
import garry.train.common.enums.ResponseEnum;
import garry.train.common.vo.ResponseVo;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Garry
 * 2024-09-25 14:25
 */
@Slf4j
@RestController
@RequestMapping(value = "/admin/job")
public class JobController {
    // 这里必须是 Autowired，不能是 Resource，
    // 否则会报错: Bean named 'schedulerFactoryBean' is expected to be of type 'org.springframework.scheduling.quartz.SchedulerFactoryBean' but was actually of type 'org.quartz.impl.StdScheduler'
    // 猜测原因是 MyFactory 中:
    //    @Override
    //    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
    //        Object jobInstance = super.createJobInstance(bundle);
    //        beanFactory.autowireBean(jobInstance);
    //        return jobInstance;
    //    }
    // `beanFactory.autowireBean(jobInstance);` 中写到了 autowire
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    /**
     * 手动立马执行一次任务
     */
    @RequestMapping(value = "/run", method = RequestMethod.POST)
    public ResponseVo run(@Valid @RequestBody CronForm form) throws SchedulerException {
        String jobClassName = form.getName();
        String jobGroupName = form.getGroup();
        log.info("手动执行任务开始: {}, {}", jobClassName, jobGroupName);

        try {
            schedulerFactoryBean.getScheduler().triggerJob(JobKey.jobKey(jobClassName, jobGroupName));

        } catch (SchedulerException e) {
            log.error("手动执行任务失败，调度异常: ", e);
            return ResponseVo.error(ResponseEnum.BATCH_SCHEDULER_RUN_FAILED_DISPATCH_ERROR);
        }
        return ResponseVo.success();
    }

    /**
     * 添加新任务，要传入全类名
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseVo add(@Valid @RequestBody CronForm form) {
        String jobClassName = form.getName();
        String jobGroupName = form.getGroup();
        String cronExpression = form.getCronExpression();
        String description = form.getDescription();
        log.info("创建定时任务开始: {}，{}，{}，{}", jobClassName, jobGroupName, cronExpression, description);

        try {
            // 通过SchedulerFactory 获取一个调度器实例
            Scheduler scheduler = schedulerFactoryBean.getScheduler();

            // 启动调度器
            scheduler.start();

            // 构建 job 信息
            JobDetail jobDetail = JobBuilder
                    .newJob((Class<? extends Job>) Class.forName(jobClassName))
                    .withIdentity(jobClassName, jobGroupName)
                    .build();

            // 表达式调度构建器(即任务执行的时间)
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

            // 按新的 cronExpression 表达式构建一个新的 trigger
            CronTrigger trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity(jobClassName, jobGroupName)
                    .withDescription(description)
                    .withSchedule(scheduleBuilder)
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.error("创建定时任务失败，调度异常: ", e);
            return ResponseVo.error(ResponseEnum.BATCH_SCHEDULER_ADD_FAILED_DISPATCH_ERROR);
        } catch (ClassNotFoundException e) {
            log.error("创建定时任务失败，任务类不存在: ", e);
            return ResponseVo.error(ResponseEnum.BATCH_SCHEDULER_ADD_FAILED_JOB_NOT_FOUND);
        }

        return ResponseVo.success();
    }

    /**
     * 暂停指定任务
     */
    @RequestMapping(value = "/pause", method = RequestMethod.POST)
    public ResponseVo pause(@Valid @RequestBody CronForm form) {
        String jobClassName = form.getName();
        String jobGroupName = form.getGroup();
        log.info("暂停定时任务开始: {}，{}", jobClassName, jobGroupName);

        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.pauseJob(JobKey.jobKey(jobClassName, jobGroupName));
        } catch (SchedulerException e) {
            log.error("暂停定时任务失败，调度异常: ", e);
            return ResponseVo.error(ResponseEnum.BATCH_SCHEDULER_PAUSE_FAILED_DISPATCH_ERROR);
        }

        return ResponseVo.success();
    }

    /**
     * 恢复指定任务
     */
    @RequestMapping(value = "/resume", method = RequestMethod.POST)
    public ResponseVo resume(@Valid @RequestBody CronForm form) {
        String jobClassName = form.getName();
        String jobGroupName = form.getGroup();
        log.info("重启定时任务开始: {}，{}", jobClassName, jobGroupName);

        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.resumeJob(JobKey.jobKey(jobClassName, jobGroupName));
        } catch (SchedulerException e) {
            log.error("重启定时任务失败，调度异常: ", e);
            return ResponseVo.error(ResponseEnum.BATCH_SCHEDULER_RESUME_FAILED_DISPATCH_ERROR);
        }

        return ResponseVo.success();
    }

    /**
     * 重定义指定任务
     */
    @RequestMapping(value = "/reschedule", method = RequestMethod.POST)
    public ResponseVo reschedule(@Valid @RequestBody CronForm form) {
        String jobClassName = form.getName();
        String jobGroupName = form.getGroup();
        String cronExpression = form.getCronExpression();
        String description = form.getDescription();
        log.info("更新定时任务开始：{}，{}，{}，{}", jobClassName, jobGroupName, cronExpression, description);

        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(jobClassName, jobGroupName);

            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            CronTriggerImpl _trigger = (CronTriggerImpl) scheduler.getTrigger(triggerKey);
            _trigger.setStartTime(new Date()); // 重新设置开始时间
            CronTrigger trigger = _trigger;

            // 按新的 cronExpression 表达式重新构建 trigger
            trigger = trigger.getTriggerBuilder()
                    .withIdentity(triggerKey)
                    .withDescription(description)
                    .withSchedule(scheduleBuilder)
                    .build();

            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);

        } catch (Exception e) {
            log.error("更新定时任务失败，调度异常: ", e);
            return ResponseVo.error(ResponseEnum.BATCH_SCHEDULER_RESCHEDULE_FAILED_DISPATCH_ERROR);
        }

        return ResponseVo.success();
    }

    /**
     * 删除指定任务
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseVo delete(@Valid @RequestBody CronForm form) {
        String jobClassName = form.getName();
        String jobGroupName = form.getGroup();
        log.info("删除定时任务开始: {}，{}", jobClassName, jobGroupName);

        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName, jobGroupName));
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName, jobGroupName));
            scheduler.deleteJob(JobKey.jobKey(jobClassName, jobGroupName));

        } catch (SchedulerException e) {
            log.error("删除定时任务失败，调度异常:", e);
            return ResponseVo.error(ResponseEnum.BATCH_SCHEDULER_DELETE_FAILED_DISPATCH_ERROR);
        }

        return ResponseVo.success();
    }

    /**
     * 查看所有任务
     */
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public ResponseVo query() {
        log.info("查看所有定时任务开始");

        List<CronVo> cronJobList = new ArrayList<>();
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            for (String groupName : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    CronVo vo = getCronVo(jobKey, scheduler);
                    cronJobList.add(vo);
                }
            }

        } catch (SchedulerException e) {
            log.error("查看定时任务失败，调度异常:", e);
            return ResponseVo.error(ResponseEnum.BATCH_SCHEDULER_QUERY_FAILED_DISPATCH_ERROR);
        }

        return ResponseVo.success(cronJobList);
    }

    private static CronVo getCronVo(JobKey jobKey, Scheduler scheduler) throws SchedulerException {
        CronVo vo = new CronVo();
        vo.setName(jobKey.getName());
        vo.setGroup(jobKey.getGroup());

        List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
        CronTrigger cronTrigger = (CronTrigger) triggers.get(0);
        vo.setDescription(cronTrigger.getDescription());
        vo.setCronExpression(cronTrigger.getCronExpression());

        Trigger.TriggerState triggerState = scheduler.getTriggerState(cronTrigger.getKey());
        vo.setState(triggerState.name());
        vo.setNextFireTime(cronTrigger.getNextFireTime());
        vo.setPreFireTime(cronTrigger.getPreviousFireTime());
        return vo;
    }
}
