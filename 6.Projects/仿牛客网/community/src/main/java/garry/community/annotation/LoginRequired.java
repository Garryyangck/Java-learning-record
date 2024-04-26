package garry.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Garry
 * ---------2024/3/20 10:23
 **/

/**
 * 在需要登录状态下才能访问的方法上使用该注解，
 * 非登录状态下的用户无法访问有该注解的方法。
 * <p>
 * tips: 加上此注解的方法可以直接调用hostHolder.getUser()，
 * 不需要担心得到的user为null
 */
/*已使用SpringSecurity代替拦截器进行权限管理，但是该注解可以方便程序员判断哪些方法需要登录，
    并且被该注解修饰的方法依然可以直接调用hostHolder而不用考虑user=null的情况*/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {
}
