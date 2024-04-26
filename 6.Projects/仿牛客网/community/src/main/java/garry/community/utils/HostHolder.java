package garry.community.utils;

/**
 * @author Garry
 * ---------2024/3/19 10:43
 **/

import garry.community.pojo.User;
import org.springframework.stereotype.Component;

/**
 * 持有用户信息，用于代替session对象
 * 该对象可以给调用它的每一个不同的线程，
 * 单独分配一个ThreadLocalMap用于set User。
 */
@Component
public class HostHolder {
    private final ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void remove() {
        users.remove();
    }
}
