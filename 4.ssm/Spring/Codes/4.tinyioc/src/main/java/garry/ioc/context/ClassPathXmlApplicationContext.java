package garry.ioc.context;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Garry
 * ---------2024/3/1 15:46
 **/
@SuppressWarnings({"all"})
//创建ClassPathXmlApplicationContext实现ApplicationContext
public class ClassPathXmlApplicationContext implements ApplicationContext {
    //通过HashMap创建iocContainer
    private Map iocContainer = new HashMap();

    public ClassPathXmlApplicationContext() {
        try {
            //通过classes相对路径获得xml配置文件的绝对路径
            String filePath = this.getClass().getResource("/applicationContext.xml").getPath();
            //采用utf-8解码，避免文件绝对路径存在中文的情况
            filePath = URLDecoder.decode(filePath, "utf-8");
            //xml文件阅读器，org.dom4j.io.SAXReader
            SAXReader reader = new SAXReader();
            //通过绝对路径创建File，让SAXReader阅读他，获得一个Document文档对象
            Document document = reader.read(new File(filePath));
            //document.getRootElement()获取根节点，筛选bean标签，获得bean标签的Node集合
            List<Node> beans = document.getRootElement().selectNodes("bean");
            //遍历beans集合
            for (Node node : beans) {
                //Node --强转为--> Element，获得当前的bean标签
                Element bean = (Element) node;
                //Element.attributeValue("xxx")获取bean标签下的id和class属性
                String id = bean.attributeValue("id");
                String className = bean.attributeValue("class");//类名绝对路径
                //使用反射获取对象
                Class<?> cls = Class.forName(className);
                Object obj = cls.newInstance();
                //获取当前的bean内部的property标签集合
                List<Node> properties = bean.selectNodes("property");
                //遍历properties集合
                for (Node nodeProperty : properties) {
                    //Node --强转为--> Element，获得当前的property标签
                    Element property = (Element) nodeProperty;
                    //Element.attributeValue("xxx")获取property标签下的name和value属性
                    String name = property.attributeValue("name");
                    String value = property.attributeValue("value");
                    //将name的首字母改为大写
                    StringBuffer buffer = new StringBuffer(name);
                    buffer.setCharAt(0, (char) (buffer.charAt(0) + ('A' - 'a')));
                    name = new String(buffer);
                    //使用反射获取set(String)方法
                    Method setMethod = cls.getMethod("set" + name, String.class);
                    //Method.invoke(obj)调用set(String)方法
                    setMethod.invoke(obj, value);
                }
                //将创建的对象加入iocContainer
                iocContainer.put(id, obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getBean(String beanId) {
        return iocContainer.get(beanId);
    }
}
