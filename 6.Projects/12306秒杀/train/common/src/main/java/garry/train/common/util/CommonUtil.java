package garry.train.common.util;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import garry.train.common.consts.CommonConst;
import garry.train.common.vo.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class CommonUtil {
    private static final Logger log = LoggerFactory.getLogger(CommonUtil.class);

    /**
     * 生成去除了 “-” 的 UUID
     *
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

    /**
     * 反序列化 ResponseVo
     * 泛型 T 强制为 String，如果需要 T，可以使用 JSONUtil.parseObj 转化为 JSONObject，读取里面的数据
     */
    public static ResponseVo<String> getResponseVo(String responseVoStr) {
        JSONObject obj = JSONUtil.parseObj(responseVoStr);
        Boolean success = obj.get("success", boolean.class);
        Integer code = obj.get("code", Integer.class);
        String msg = obj.get("msg", String.class);
        String data = obj.get("data", String.class); // 将 T 强转为 String
        Object[] args = {success, code, msg, data};
        try {
            Constructor<ResponseVo> constructor = ResponseVo.class.getDeclaredConstructor(boolean.class, Integer.class, String.class, String.class);
            constructor.setAccessible(true);
            return (ResponseVo<String>) constructor.newInstance(args);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            log.error("反序列化 ResponseVo 失败，responseVoStr = {}", responseVoStr);
            throw new RuntimeException(e);
        }
    }
}
