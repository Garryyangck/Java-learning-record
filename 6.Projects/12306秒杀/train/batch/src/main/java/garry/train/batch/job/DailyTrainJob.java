package garry.train.batch.job;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import garry.train.batch.feign.BusinessFeign;
import garry.train.common.consts.CommonConst;
import garry.train.common.util.CommonUtil;
import garry.train.common.vo.ResponseVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Garry
 * 2024-09-28 21:19
 */
@Slf4j
@Component
@DisallowConcurrentExecution // 禁止任务并发执行
public class DailyTrainJob implements Job {
    @Resource
    private BusinessFeign businessFeign;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        DateTime dateTime = DateUtil.offsetDay(new Date(), CommonConst.DAILY_TRAIN_OFFSET_DAYS);
        Date date = dateTime.toJdkDate();
        String voStr = businessFeign.genDaily(date);
        ResponseVo<String> responseVo = CommonUtil.getResponseVo(voStr);
        if (responseVo.isSuccess()) {
            log.info("成功为 {} 插入所有车次相关数据", date);
        } else {
            log.error("未能为 {} 插入所有车次相关数据", date);
        }
    }
}
