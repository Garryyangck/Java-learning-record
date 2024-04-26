package garry.ioc;

import garry.ioc.context.ApplicationContext;
import garry.ioc.context.ClassPathXmlApplicationContext;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * @author Garry
 * ---------2024/3/1 16:51
 **/
@SuppressWarnings({"all"})
public class Application {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        //采用自己创建的ClassPathXmlApplicationContext加载xml配置文件
        ApplicationContext context = new ClassPathXmlApplicationContext();
        //读取properties文件
        Properties properties = new Properties();
        properties.load(new FileInputStream("D:\\杨宸楷\\学习\\java-learning\\4.ssm\\Spring\\Codes\\4.tinyioc\\src\\main\\java\\garry\\ioc\\ids.properties"));
        //通过Properties获取beanId和method
        String beanId = properties.getProperty("beanId");
        String methodName = properties.getProperty("method");
        //通过id获得sweetApple对象
        Object obj = context.getBean(beanId);
        //获取运行类型
        Class<?> cls = obj.getClass();
        //获取Method对象
        Method method = cls.getMethod(methodName);
        //Method.invoke调用相关方法
        method.invoke(obj);
    }
}
