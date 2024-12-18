package garry.train.business.config;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@ComponentScan("garry") // 由于Application类放到了config包下，它只能扫描和自己同包的类，因此需要新增ComponentScan注解让其扫描整个garry包下的类
@MapperScan("garry.train.business.mapper") // 扫描mybatis的代码
@EnableTransactionManagement // 允许事务处理
@EnableAsync // 允许异步执行
@EnableFeignClients("garry.train.business.feign")
public class BusinessApplication {
    public static void main(String[] args) {
        // 打印启动日志
        SpringApplication app = new SpringApplication(BusinessApplication.class);
        Environment env = app.run(args).getEnvironment(); // 注意，这一句已经app.run了，因此不能再额外run了
        log.info("启动成功！");
        log.info("地址\thttp://127.0.0.1:{}{}/hello", env.getProperty("server.port"), env.getProperty("server.servlet.context-path"));
//        initFlowRules();
//        log.info("Sentinel 限流规则定义成功！");
    }

//    private static void initFlowRules() {
//        List<FlowRule> rules = new ArrayList<>();
//        FlowRule rule = new FlowRule();
//        rule.setResource("doConfirm");
//        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
//        // Set limit QPS to 20.
//        rule.setCount(1);
//        rules.add(rule);
//        FlowRuleManager.loadRules(rules);
//    }
}
