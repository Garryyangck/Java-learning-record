package garry.spring.aop.service;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Garry
 * ---------2024/3/2 17:52
 **/

/**
 * InvocationHandler是JDK提供的反射类,用于在IDK动态代理中对目标力法进行增强
 * InvocationHandler实现类与切面类的环绕通知类似
 */
@SuppressWarnings({"all"})
public class ProxyInvocationHandler implements InvocationHandler {
    private Object target;//目标对象

    public ProxyInvocationHandler(Object target) {
        this.target = target;
    }

    /**
     * 在invoke()方法对目标方法进行增强
     *
     * @param proxy  代理类对象
     * @param method 目标方法对象
     * @param args   目标方法对象
     * @return 目标方法运行后返回值
     * @throws Throwable 目标方法抛出的异常
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("=========" + new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss.SSS]").format(new Date()) + "=========");
        //ProceedingJoinPoint.proceeding的本质
        Object ret = method.invoke(target, args);
        return ret;
    }

    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        ProxyInvocationHandler invocationHandler =
                new ProxyInvocationHandler(userService);

        //动态创建Proxy代理类
        UserService proxyUserServiceInstance = (UserService) Proxy.newProxyInstance
                (userService.getClass().getClassLoader(),//类加载器
                        userService.getClass().getInterfaces(),//实现的接口
                        invocationHandler);//反射类
        proxyUserServiceInstance.createUser();

        //同理代理EmploeeService的EmploeeServiceImpl实现类
        EmployeeService employeeService = new EmployeeServiceImpl();
        EmployeeService proxyEmployeeServiceInstance = (EmployeeService) Proxy.newProxyInstance
                (employeeService.getClass().getClassLoader(),
                        employeeService.getClass().getInterfaces(),
                        new ProxyInvocationHandler(employeeService));
        proxyEmployeeServiceInstance.createEmployee();
    }
}
