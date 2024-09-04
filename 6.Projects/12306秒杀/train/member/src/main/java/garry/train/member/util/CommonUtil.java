package garry.train.member.util;

import java.util.UUID;

public class CommonUtil {
    public static String generateUUID(int limit) {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, limit);
    }
}
