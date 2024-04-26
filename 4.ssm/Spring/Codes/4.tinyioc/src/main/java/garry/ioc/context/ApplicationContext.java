package garry.ioc.context;

/**
 * @author Garry
 * ---------2024/3/1 15:45
 **/
public interface ApplicationContext {
    public Object getBean(String beanId);
}
