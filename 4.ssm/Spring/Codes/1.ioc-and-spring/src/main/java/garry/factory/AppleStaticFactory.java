package garry.factory;

import garry.pojo.Apple;

/**
 * @author Garry
 * ---------2024/2/29 20:45
 **/
public class AppleStaticFactory {
    public static Apple createSoftApple() {
        return new Apple("金帅", "中国", "黄色");
    }
}
