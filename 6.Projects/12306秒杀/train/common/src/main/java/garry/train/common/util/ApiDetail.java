package garry.train.common.util;

import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Garry
 * 2024-10-14 20:16
 */

/**
 * 只在内存中计算，即每次开机时的 api 数据
 */
@Data
public class ApiDetail {

    @Getter
    private final static Map<String, ApiDetail> apiDetailMap;

    static {
        apiDetailMap = new HashMap<>();
    }

    /**
     * 创建一个崭新的 ApiDetail 对象
     */
    public static ApiDetail newApiDetailInstance(String fullApiPath, String apiMethod, String moduleName, Long mills, Boolean success) {
        ApiDetail apiDetail = new ApiDetail();
        apiDetail.setFullApiPath(fullApiPath);
        apiDetail.setApiMethod(apiMethod);
        apiDetail.setModuleName(moduleName);
        apiDetail.setCallTimes(BigDecimal.ONE);
        apiDetail.setSuccessTimes(success ? BigDecimal.ONE : BigDecimal.ZERO);
        apiDetail.setSuccessRatio(success ? "100.00%" : "0.00%");
        apiDetail.setMaxExecuteMills(BigDecimal.valueOf(mills));
        apiDetail.setMinExecuteMills(BigDecimal.valueOf(mills));
        apiDetail.setExecuteMills(BigDecimal.valueOf(mills));
        apiDetail.setSuccessExecuteMills(success ? BigDecimal.valueOf(mills) : BigDecimal.ZERO);
        apiDetail.setAvgExecuteMills(BigDecimal.valueOf(mills));
        apiDetail.setAvgSuccessExecuteMills(success ? BigDecimal.valueOf(mills) : BigDecimal.ZERO);
        return apiDetail;
    }

    /**
     * 向 apiDetailMap 中插入 apiDetail
     */
    public static void putApiDetails(ApiDetail apiDetail) {
        if (apiDetailMap.containsKey(apiDetail.getFullApiPath())) {
            apiDetailMap.put(apiDetail.getFullApiPath(), mergeApiDetail(
                    apiDetailMap.get(apiDetail.getFullApiPath()),
                    apiDetail
            ));
        } else {
            apiDetailMap.put(apiDetail.getFullApiPath(), apiDetail);
        }
    }

    /**
     * 向 apiDetailMap 中插入 apiDetail
     */
    public static void putApiDetails(String fullApiPath, String apiMethod, String moduleName, Long mills, Boolean success) {
        putApiDetails(newApiDetailInstance(fullApiPath, apiMethod, moduleName, mills, success));
    }

    /**
     * 将两个相同的方法的 ApiDetail，合并到一起
     */
    public static ApiDetail mergeApiDetail(ApiDetail oldApiDetail, ApiDetail newApiDetail) {
        if (!oldApiDetail.getFullApiPath().equals(newApiDetail.getFullApiPath())) {
            throw new IllegalArgumentException("The fullApiPath of the two ApiDetail instances must be the same.");
        }

        // 更新调用次数
        BigDecimal newCallTimes = oldApiDetail.getCallTimes().add(newApiDetail.getCallTimes());

        // 更新成功调用次数
        BigDecimal newSuccessTimes = oldApiDetail.getSuccessTimes().add(newApiDetail.getSuccessTimes());

        // 更新执行总时间(ms)
        BigDecimal newExecuteMills = oldApiDetail.getExecuteMills().add(newApiDetail.getExecuteMills());

        // 更新成功执行总时间(ms)
        BigDecimal newSuccessExecuteMills = oldApiDetail.getSuccessExecuteMills().add(newApiDetail.getSuccessExecuteMills());

        // 更新最长执行时间(ms)
        BigDecimal newMaxExecuteMills = oldApiDetail.getMaxExecuteMills().max(newApiDetail.getMaxExecuteMills());

        // 更新最短执行时间(ms)
        BigDecimal newMinExecuteMills = oldApiDetail.getMinExecuteMills().min(newApiDetail.getMinExecuteMills());

        // 计算新的成功比例
        String newSuccessRatio = newCallTimes.compareTo(BigDecimal.ZERO) == 0 ? "0.00%" :
                String.format("%.2f%%", newSuccessTimes.divide(newCallTimes, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).doubleValue());

        // 计算新的平均执行时间(ms)
        BigDecimal newAvgExecuteMills = newCallTimes.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO :
                newExecuteMills.divide(newCallTimes, 2, RoundingMode.HALF_UP);

        // 计算新的成功平均执行时间(ms)
        BigDecimal newAvgSuccessExecuteMills = newSuccessTimes.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO :
                newSuccessExecuteMills.divide(newSuccessTimes, 2, RoundingMode.HALF_UP);

        // 创建一个新的 ApiDetail 实例并设置合并后的值
        ApiDetail mergedApiDetail = new ApiDetail();

        // 设置合并后的值
        mergedApiDetail.setFullApiPath(oldApiDetail.getFullApiPath());
        mergedApiDetail.setApiMethod(oldApiDetail.getApiMethod());
        mergedApiDetail.setModuleName(oldApiDetail.getModuleName());
        mergedApiDetail.setCallTimes(newCallTimes);
        mergedApiDetail.setSuccessTimes(newSuccessTimes);
        mergedApiDetail.setSuccessRatio(newSuccessRatio);
        mergedApiDetail.setMaxExecuteMills(newMaxExecuteMills);
        mergedApiDetail.setMinExecuteMills(newMinExecuteMills);
        mergedApiDetail.setExecuteMills(newExecuteMills);
        mergedApiDetail.setSuccessExecuteMills(newSuccessExecuteMills);
        mergedApiDetail.setAvgExecuteMills(newAvgExecuteMills);
        mergedApiDetail.setAvgSuccessExecuteMills(newAvgSuccessExecuteMills);

        return mergedApiDetail;
    }

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
     * 成功执行总时间(ms)
     */
    private BigDecimal successExecuteMills;

    /**
     * 平均执行时间(ms)
     */
    private BigDecimal avgExecuteMills;

    /**
     * 成功平均执行时间(ms)
     */
    private BigDecimal avgSuccessExecuteMills;
}
