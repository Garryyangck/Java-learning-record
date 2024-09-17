package garry.train.generator.gen;

import freemarker.template.TemplateException;
import garry.train.generator.util.DBUtil;
import garry.train.generator.util.Field;
import garry.train.generator.util.FreemarkerUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

public class ServerGenerator {

    private static String pomPath = "generator/pom.xml/";

    private static String serverPath = "[module]/src/main/java/garry/train/[module]/";

    public static void main(String[] args) throws Exception {
        // 获取 mybatis-generator 配置文件的路径
        String generatorPath = getGeneratorPath();

        // 获取 module，替换 serverPath 中的 [module]
        String module = generatorPath.replace("src/main/resources/generator-config-", "").replace(".xml", "");
        System.out.println("module = " + module);
        serverPath = serverPath.replace("[module]", module);

        // 获取 tableName 和 domainObjectName
        Document document = new SAXReader().read("generator/" + generatorPath);
        Node table = document.selectSingleNode("//table");
        System.out.println("table = " + table);
        Node tableName = table.selectSingleNode("@tableName");
        Node domainObjectName = table.selectSingleNode("@domainObjectName");
        System.out.println("表名: " + tableName.getText() + " / " + "对象名: " + domainObjectName.getText());

        // 获取数据库连接的参数
        Node jdbcConnection = document.selectSingleNode("//jdbcConnection");
        Node connectionURL = jdbcConnection.selectSingleNode("@connectionURL");
        System.out.println("connectionURL = " + connectionURL.getText());
        Node userId = jdbcConnection.selectSingleNode("@userId");
        System.out.println("userId = " + userId.getText());
        Node password = jdbcConnection.selectSingleNode("@password");
        System.out.println("password = " + password.getText());
        DBUtil.url = connectionURL.getText();
        DBUtil.user = userId.getText();
        DBUtil.password = password.getText();

        // 示例：表名 garry_test
        // GarryTest，类名
        String Domain = domainObjectName.getText();
        // garryTest，属性变量名
        String domain = Domain.substring(0, 1).toLowerCase() + Domain.substring(1);
        // garry-test，url 名
        String do_main = tableName.getText().replace("_", "-");
        // 表中文名
        String tableNameCn = DBUtil.getTableComment(tableName.getText());
        List<Field> fieldList = DBUtil.getColumnByTableName(tableName.getText());

        // 组装参数
        HashMap<String, Object> param = new HashMap<>();
        param.put("Domain", Domain);
        param.put("domain", domain);
        param.put("do_main", do_main);
        param.put("DateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        System.out.println("组装参数: " + param);

        // 生成代码
        generate(Domain, param, "service");
        generate(Domain, param, "service-impl");
        generate(Domain, param, "controller");
    }

    /**
     * 执行代码生成
     *
     * @param Domain pojo 类名，e.g Passenger
     * @param param  参数哈希表
     * @param target ftl模板名，e.g service-impl 或 service
     */
    private static void generate(String Domain, HashMap<String, Object> param, String target) throws IOException, TemplateException {
        FreemarkerUtil.initConfig(target + ".ftl");
        String[] strings = target.split("-");
        StringBuilder suffixClass = new StringBuilder(); // 类名的后缀，如 Service, ServiceImpl
        StringBuilder suffixPath = new StringBuilder(); // 路径的后缀，如 service/, service/impl/
        for (String str : strings) {
            suffixPath.append(str).append("/");
            suffixClass.append(str.substring(0, 1).toUpperCase()).append(str.substring(1));
        }
        String toPath = serverPath + suffixPath;
        System.out.println("toPath = " + toPath);
        new File(toPath).mkdirs(); // 生成 toPath 路径，避免生成时还没有这个路径
        String fullClassName = Domain + suffixClass + ".java";
        System.out.println("fullClassName = " + fullClassName);
        String fullPath = toPath + fullClassName;
        System.out.println("fullPath = " + fullPath);
        FreemarkerUtil.generator(fullPath, param);
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
