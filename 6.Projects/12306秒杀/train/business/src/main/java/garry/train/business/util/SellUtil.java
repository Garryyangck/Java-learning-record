package garry.train.business.util;

/**
 * @author Garry
 * 2024-10-09 00:51
 */

import cn.hutool.core.util.StrUtil;
import garry.train.common.enums.ResponseEnum;
import garry.train.common.exception.BusinessException;

/**
 * 专门处理 DailyTrainSeat.sell 字段的工具类
 * <p>
 * 注意，sell 的二进制数的低位(右侧)为较小的车站站序
 * </p>
 * 比如：sell = 0001 表示第一站到第二站的车票已卖出，而第二章到第五站的车票未卖出
 */
public class SellUtil {

    /**
     * @param startIndex startIndex >= 1
     * @param endIndex   endIndex >= 1
     * @return sell 中从 startIndex 到 endIndex 的票是否已经被卖了
     */
    public static boolean isSold(Integer sell, Integer startIndex, Integer endIndex) {
        int mask = ((1 << (endIndex - startIndex)) - 1) << (startIndex - 1);
        return (mask & sell) != 0;
    }

    /**
     * 尝试卖出 sell 中从 startIndex 到 endIndex 的票
     * 在卖之前要先校验是否有票
     *
     * @param startIndex startIndex >= 1
     * @param endIndex   endIndex >= 1
     * @return 售卖完成后的 sell
     */
    public static Integer sell(Integer sell, Integer startIndex, Integer endIndex) {
        if (!isSold(sell, startIndex, endIndex)) {
            int mask = ((1 << (endIndex - startIndex)) - 1) << (startIndex - 1);
            return sell | mask;
        } else {
            throw new BusinessException(ResponseEnum.BUSINESS_SELL_FAILED_LACK_OF_TICKETS);
        }
    }

    /**
     * 通过 sell 获取传给前端的 BinaryString
     * <p>
     * 由于我们的 sell 是低位代表小车站，而前端的展示需要高位代表小车站
     * 因此需要前置填充 0，然后反转字符串
     * </p>
     */
    public static String getBinaryString(Integer sell, Integer binaryStringLen) {
        return StrUtil.reverse(
                StrUtil.fillBefore(
                        Integer.toBinaryString(sell),
                        '0',
                        binaryStringLen
                ));
    }

    public static void main(String[] args) {
        Integer sell = 0;
        sell = sell(sell, 1, 2);
        sell = sell(sell, 4, 5);
        System.out.println(getBinaryString(sell, 4));
        System.out.println(isSold(sell, 2, 3));
        System.out.println(isSold(sell, 3, 4));
        sell(sell, 1, 5);
    }
}
