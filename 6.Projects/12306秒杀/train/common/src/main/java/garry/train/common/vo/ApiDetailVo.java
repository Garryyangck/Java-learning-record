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
     * 接口的全路径
     */
    private String FullApiPath;

    /**
     * 接口的类型 | GET, POST...
     */
    private String ApiMethod;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 被调用的次数
     */
    private BigDecimal callTimes;

    /**
     * 成功调用的次数
     */
    private BigDecimal successTimes;

    /**
     * 成功的比例 | 33.33%
     */
    private String successRatio;

    /**
     * 执行的总毫秒
     */
    private BigDecimal executeMills;

    /**
     * 成功调用执行的总毫秒
     */
    private BigDecimal successExecuteMills;

    /**
     * 平均执行总毫秒
     */
    private BigDecimal avgExecuteMills;

    /**
     * 成功调用执行的平均总毫秒
     */
    private BigDecimal avgSuccessExecuteMills;
}
