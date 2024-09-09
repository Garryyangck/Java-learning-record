package garry.train.common.util;

import cn.hutool.core.util.IdUtil;
import garry.train.common.consts.CommonConst;

import java.util.UUID;

public class CommonUtil {
    public static String generateUUID(int limit) {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, limit);
    }

    public static long getSnowflakeNextId() {
        return IdUtil.getSnowflake(CommonConst.WORKER_ID, CommonConst.DATACENTER_ID).nextId();
    }
}
