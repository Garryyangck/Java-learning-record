package garry.train.common.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Garry
 * 2024-10-14 20:16
 */

/**
 * 只在内存中计算，即每次开机时的 api 数据
 */
@Data
public class ApiDetailVo {

    /**
     * 接口全路径
     */
    private String fullApiPath;

    /**
     * 类型 | GET, POST...
     */
    private String apiMethod;

    /**
     * 模块
     */
    private String moduleName;

    /**
     * 调用次数
     */
    private BigDecimal callTimes;

    /**
     * 成功调用次数
     */
    private BigDecimal successTimes;

    /**
     * 成功比例 | 33.33%
     */
    private String successRatio;

    /**
     * 最长执行时间(ms)
     */
    private BigDecimal maxExecuteMills;

    /**
     * 最短执行时间(ms)
     */
    private BigDecimal minExecuteMills;

    /**
     * 执行总时间(ms)
     */
    private BigDecimal executeMills;

    /**
     * 平均执行时间(ms)
     */
    private BigDecimal avgExecuteMills;
}
