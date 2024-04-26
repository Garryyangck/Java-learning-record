package garry.factory;

import garry.pojo.Apple;

/**
 * @author Garry
 * ---------2024/2/29 20:52
 **/
public class AppleFactoryInstance {
    public Apple createSourApple() {
        return new Apple("青苹果", "中亚", "绿色");
    }
}
