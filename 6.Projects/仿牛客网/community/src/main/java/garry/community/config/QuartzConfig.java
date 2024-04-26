package garry.community.config;

import garry.community.consts.CommunityConst;
import garry.community.quartz.PostScoreRefreshJob;
import garry.community.quartz.testJob;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/**
 * @author Garry
 * ---------2024/3/30 11:56
 **/

/**
 * 该配置类只有在第一次被读取时，会将其配置内容存储到数据库中，
 * 之后就直接读取数据库获取配置，而不再读取此配置类了。
 * <p>
 * Quartz 发现数据库中有某个 Job 的 Detail 配置和触发器 Trigger 配置，
 * 就会自动的按配置执行该任务
 */
@Configuration
public class QuartzConfig {

    /**
     * FactoryBean 的解释:
     * FactoryBean 可简化 Bean 的实例化过程
     * 1.FactoryBean 封装 Bean 的实例化过程
     * 2.将 FactoryBean 装配到 ioc 容器
     * 3.将 FactoryBean 注入到其它 Bean
     * 4.其它 Bean 得到的是 FactoryBean 所管理的 Bean 对象实例
     * <p>
     * 配置 JobDetail
     *
     * @return
     */
    //@Bean
    public JobDetailFactoryBean testJobDetail() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(testJob.class);
        factoryBean.setName("testJob");
        factoryBean.setGroup("testJobGroup");
        factoryBean.setDurability(true);//任务是否持久保存
        factoryBean.setRequestsRecovery(true);//任务是否可被恢复
        return factoryBean;
    }

    /**
     * 配置 Trigger
     * <p>
     * 可以直接获取 FactoryBean 中封装的 Bean 对象实例 (for example: xxxJobDetail)
     *
     * @param testJobDetail
     * @return
     */
    //@Bean
    public SimpleTriggerFactoryBean testTrigger(JobDetail testJobDetail) {
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(testJobDetail);
        factoryBean.setName("testTrigger");
        factoryBean.setGroup("testTriggerGroup");
        factoryBean.setRepeatInterval(3000);//执行频率
        factoryBean.setJobDataMap(new JobDataMap());//用什么对象存储job的状态
        return factoryBean;
    }

    @Bean
    public JobDetailFactoryBean postScoreRefreshJobDetail() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(PostScoreRefreshJob.class);
        factoryBean.setName("postScoreRefreshJob");
        factoryBean.setGroup("communityJobGroup");
        factoryBean.setDurability(true);//任务是否持久保存
        factoryBean.setRequestsRecovery(true);//任务是否可被恢复
        return factoryBean;
    }

    @Bean
    public SimpleTriggerFactoryBean PostScoreRefreshTrigger(JobDetail postScoreRefreshJobDetail) {
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(postScoreRefreshJobDetail);
        factoryBean.setName("postScoreRefreshTrigger");
        factoryBean.setGroup("communityTriggerGroup");
        factoryBean.setRepeatInterval(CommunityConst.POST_REFRESH_INTERVAL);//执行频率
        factoryBean.setJobDataMap(new JobDataMap());//用什么对象存储job的状态
        return factoryBean;
    }
}
