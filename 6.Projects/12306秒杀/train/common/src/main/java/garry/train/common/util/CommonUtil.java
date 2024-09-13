package garry.train.common.util;

import cn.hutool.core.util.IdUtil;
import garry.train.common.consts.CommonConst;

import java.util.UUID;

public class CommonUtil {
    /**
     * 生成去除了 “-” 的 UUID
     * @param limit UUID 的位数
     */
    public static String generateUUID(int limit) {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, limit);
    }

    /**
     * 使用雪花算法生成 ID
     */
    public static long getSnowflakeNextId() {
        return IdUtil.getSnowflake(CommonConst.WORKER_ID, CommonConst.DATACENTER_ID).nextId();
    }
}
