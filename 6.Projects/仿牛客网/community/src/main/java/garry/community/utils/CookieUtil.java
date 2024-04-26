package garry.community.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Garry
 * ---------2024/3/19 10:23
 **/

public class CookieUtil {
    /**
     * 获取request中名为cookieName的Cookie的value
     * 如果不存在此cookie则返回null
     *
     * @param request
     * @param cookieName
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        if (request == null || cookieName == null) {
            throw new IllegalArgumentException("【参数为空】");
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
