package garry.spring.aop.aspect;

/**
 * @author Garry
 * ---------2024/3/2 11:41
 **/

import org.aspectj.lang.JoinPoint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 切面类
 */
@SuppressWarnings({"all"})
public class MethodAspect {
    /**
     * 切面方法，打印执行时间
     * 切面方法必须要有JoinPoint连接点，获取目标类，目标方法的相关信息
     *
     * @param joinPoint
     */
    public void printExecutionTime(JoinPoint joinPoint) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss.SSS]");
        String now = dateFormat.format(new Date());

        //joinPoint获取执行的对象，对象的类，类的名称
        String className = joinPoint.getTarget().getClass().getName();
        //joinPoint获取对象的方法，方法的名称
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        String strArgs = "";
        for (Object arg : args) {
            strArgs += arg.getClass().getSimpleName() + ", ";
        }
        strArgs = args.length == 0 ? "" : strArgs.substring(0, strArgs.length() - 2);
        System.out.println("----->" + now + ":" +
                className + "." + methodName + "(" + strArgs + ")");
        System.out.println("----->参数个数:" + args.length);
        for (Object arg : args) {
            System.out.println("----->参数:" + arg);
        }
    }

    /**
     * 后置通知
     *
     * @param joinPoint
     */
    public void doAfter(JoinPoint joinPoint) {
        System.out.println("<-----后置通知");
    }

    /**
     * 返回后通知
     *
     * @param joinPoint
     * @param obj
     */
    public void doAfterReturn(JoinPoint joinPoint, Object ret) {
        System.out.println("<-----返回后通知:" + ret);
    }

    /**
     * 异常通知
     *
     * @param joinPoint
     * @param th
     */
    private void doAfterException(JoinPoint joinPoint, Throwable th) {
        System.out.println("<-----异常通知:" + th.getMessage());
    }
}
