package garry.mall.consts;

import lombok.Getter;

/**
 * @author Garry
 * ---------2024/3/7 20:45
 **/

/**
 * mall项目中的一些常量
 */
@Getter
public class MallConst {
    public static final String CURRENT_USER = "currentUser";//当前登录用户

    public static final Integer ROOT_PARENT_ID = 0;//根目录id

    public static final String CART_REDIS_KEY_TEMPLATE = "cart_%s";//购物车id模板
}
