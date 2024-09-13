package garry.train.common.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import garry.train.common.consts.CommonConst;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * @author Garry
 * 2024-09-12 15:22
 */
@Slf4j
public class JWTUtil {

    private static final String key = "theBravestGarry20240201";

    /**
     * 生成 JWT
     */
    public static String createToken(Long id, String mobile) {
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("id", id);
        payload.put("mobile", mobile);

        DateTime now = DateTime.now();
        DateTime expireTime = now.offsetNew(DateField.HOUR, CommonConst.JWT_EXPIRE_HOUR);
        payload.put(JWTPayload.ISSUED_AT, now); // 签发时间
        payload.put(JWTPayload.EXPIRES_AT, expireTime); // 过期时间
        payload.put(JWTPayload.NOT_BEFORE, now); // 生效时间

        String token = cn.hutool.jwt.JWTUtil.createToken(payload, key.getBytes());
        log.info("已为手机号 {} 的用户生成 JWT: {}", mobile, token);
        return token;
    }

    /**
     * 校验 token 是否有效
     */
    public static boolean validate(String token) {
        try {
            JWT jwt = JWT.of(token).setKey(key.getBytes());
            return jwt.validate(0);
        } catch (Exception e) {
            log.error("校验异常", e);
            return false;
        }
    }

    /**
     * 获取 JWT 中的原始内容
     */
    public static JSONObject getJSONObject(String token) {
        validate(token);
        JWT jwt = JWT.of(token).setKey(key.getBytes());
        JSONObject payloads = jwt.getPayloads();
        payloads.remove(JWTPayload.ISSUED_AT);
        payloads.remove(JWTPayload.EXPIRES_AT);
        payloads.remove(JWTPayload.NOT_BEFORE);
        log.info("根据token获取的原始内容: {}", payloads);
        return payloads;
    }
}
