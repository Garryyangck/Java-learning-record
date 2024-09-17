package garry.train.generator.gen;

import garry.train.generator.util.FreemarkerUtil;
import lombok.Data;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class ServerGenerator {
    @Data
    private static class GeneratorResult {
        private String tableName;
        private String domainObjectName;

        public GeneratorResult(String tableName, String domainObjectName) {
            this.tableName = tableName;
            this.domainObjectName = domainObjectName;
        }
    }

    private static String pomPath = "generator/pom.xml/";

    private static String servicePath = "[module]/src/main/java/garry/train/[module]/service/";

    private static String serviceImplPath = "";

    public static void main(String[] args) throws Exception {
        // 获取 mybatis-generator 配置文件的路径
        String generatorPath = getGeneratorPath();

        // 获取 module，替换 xxxPath 中的 [module]
        String module = generatorPath.replace("src/main/resources/generator-config-", "").replace(".xml", "");
        System.out.println("module = " + module);
        servicePath = servicePath.replace("[module]", module);
        serviceImplPath = servicePath + "impl/";

        // 获取 tableName 和 domainObjectName
        Document document = new SAXReader().read("generator/" + generatorPath);
        Node table = document.selectSingleNode("//table");
        System.out.println("table = " + table);
        Node tableName = table.selectSingleNode("@tableName");
        Node domainObjectName = table.selectSingleNode("@domainObjectName");
        System.out.println("表名: " + tableName.getText() + " / " + "对象名: " +  domainObjectName.getText());

        // 表名 garry_test
        // GarryTest，类名
        String Domain = domainObjectName.getText();
        // garryTest，属性变量名
        String domain = Domain.substring(0, 1).toLowerCase() + Domain.substring(1);
        // garry-test，url 名
        String do_main = tableName.getText().replace("_", "-");

        // 组装参数
        HashMap<String, Object> param = new HashMap<>();
        param.put("Domain", Domain);
        param.put("domain", domain);
        param.put("do_main", do_main);
        param.put("DateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        System.out.println("组装参数: " + param);

        // 生成 service 层代码
        FreemarkerUtil.initConfig("service.ftl");
        FreemarkerUtil.generator(servicePath + Domain + "Service.java", param);
        FreemarkerUtil.initConfig("service-impl.ftl");
        FreemarkerUtil.generator(serviceImplPath + Domain + "ServiceImpl.java", param);
    }

    /**
     * 从 generator/pom.xml/ 中获取 Mybatis-generator 配置文件的路径
     */
    private static String getGeneratorPath() throws DocumentException {
        SAXReader saxReader = new SAXReader();
        HashMap<String, String> map = new HashMap<>();
        map.put("pom", "http://maven.apache.org/POM/4.0.0");
        saxReader.getDocumentFactory().setXPathNamespaceURIs(map);
        Document document = saxReader.read(pomPath);
        /*
          使用 XPATH 在 xml 文件中找寻所需的标签
          解释 "//pom:configurationFile"：
          // : 从根目录下寻找
          pom : xml 的命名空间
          configurationFile : 节点名
          若要找 configurationFile 下的某属性，就用:
          Node.selectSingleNode("@propertyName")
         */
        Node node = document.selectSingleNode("//pom:configurationFile");
        System.out.println("generatorPath = " + node.getText());
        return node.getText();
    }
}
