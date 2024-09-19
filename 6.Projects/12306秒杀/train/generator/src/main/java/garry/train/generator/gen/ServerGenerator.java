package garry.train.generator.gen;

import cn.hutool.json.JSONUtil;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ServerGenerator {

    private static String pomPath = "generator/pom.xml/";

    private static String serverPath = "[module]/src/main/java/garry/train/[module]/";

    private static String vuePath = "[isAdmin]/src/views/main/";

    private static String module = "";

    public static void main(String[] args) throws Exception {
        // 获取 mybatis-generator 配置文件的路径
        String generatorPath = getGeneratorPath();

        // 获取 module，替换 serverPath 中的 [module]
        module = generatorPath.replace("src/main/resources/generator-config-", "").replace(".xml", "");
        System.out.println("module = " + module);
        serverPath = serverPath.replace("[module]", module);

        Document document = new SAXReader().read("generator/" + generatorPath);
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

        // 遍历每一个 table
        List<Node> tables = document.selectNodes("//table");
        for (Node table : tables) {
            Node tableName = table.selectSingleNode("@tableName");
            Node domainObjectName = table.selectSingleNode("@domainObjectName");
            System.out.println("tableName: " + tableName.getText() + " / " + "domainObjectName: " + domainObjectName.getText());

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
            Set<String> typeSet = getJavaTypes(fieldList);

            // 组装参数
            HashMap<String, Object> param = new HashMap<>();
            param.put("module", module);
            param.put("Domain", Domain);
            param.put("domain", domain);
            param.put("do_main", do_main);
            param.put("DateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            param.put("tableNameCn", tableNameCn);
            param.put("fieldList", fieldList);
            param.put("typeSet", typeSet);
            System.out.println("组装参数: " + JSONUtil.toJsonPrettyStr(param));

            generateAll(Domain, do_main, param, true);
        }
    }

    private static void generateAll(String Domain, String do_main, HashMap<String, Object> param, Boolean isAdmin) throws IOException, TemplateException {
        generateBackend(Domain, param, isAdmin);
        generateVue(do_main, param, false, isAdmin);
    }

    /**
     * 执行代码生成
     *
     * @param Domain      pojo 类名，Passenger
     * @param param       额外携带的参数
     * @param packageName 包名，service/impl/，vo/，form/
     * @param target      freemarker 模板名，service-impl，save-vo，save-form
     */
    private static void generate(String Domain, HashMap<String, Object> param, String packageName, String target) throws IOException, TemplateException {
        System.out.println("\n------------- generate 开始 -------------");
        FreemarkerUtil.initConfig(target + ".ftl"); // service-impl.ftl
        String[] strings = target.split("-"); // ["service", "impl"]
        StringBuilder suffixClass = new StringBuilder(); // 类名的后缀，ServiceImpl
        for (String str : strings) {
            suffixClass.append(str.substring(0, 1).toUpperCase()).append(str.substring(1));
        }
        String toPath = serverPath + packageName; // [module]/src/main/java/garry/train/[module]/service/impl/
        System.out.println("toPath = " + toPath);
        new File(toPath).mkdirs(); // 生成 toPath 路径，避免生成时还没有这个路径
        String fullClassName = (Domain + suffixClass + ".java").replace("Pojo", ""); // PassengerServiceImpl.java
        System.out.println("fullClassName = " + fullClassName);
        String fullPath = toPath + fullClassName; // [module]/src/main/java/garry/train/[module]/service/impl/PassengerServiceImpl.java
        System.out.println("fullPath = " + fullPath);
        FreemarkerUtil.generator(fullPath, param);
        System.out.println("------------- generate 结束 -------------\n");
    }

    /**
     * 生成后端代码
     */
    private static void generateBackend(String Domain, HashMap<String, Object> param, Boolean isAdmin) throws IOException, TemplateException {
        generate(Domain, param, "pojo/", "pojo");
        generate(Domain, param, "form/", "save-form");
        generate(Domain, param, "form/", "query-form");
        generate(Domain, param, "vo/", "query-vo");
        generate(Domain, param, "service/", "service");
        generate(Domain, param, "service/impl/", "service-impl");
        if (!isAdmin)
            generate(Domain, param, "controller/", "controller");
        else
            generate(Domain, param, "controller/admin/", "admin-controller");
    }

    /**
     * 专门生成前端 vue 页面的生成器
     */
    private static void generateVue(String do_main, HashMap<String, Object> param, Boolean readOnly, Boolean isAdmin) throws IOException, TemplateException {
        System.out.println("\n------------- generateVue 开始 -------------");
        param.put("readOnly", readOnly);
        String fullPath = "";
        if (!isAdmin) {
            FreemarkerUtil.initConfig("vue.ftl");
            vuePath = vuePath.replace("[isAdmin]", "web");
            new File(vuePath).mkdirs();
            fullPath = vuePath + do_main + ".vue";
        } else {
            FreemarkerUtil.initConfig("admin-vue.ftl");
            vuePath = vuePath.replace("[isAdmin]", "admin");
            new File(vuePath).mkdirs();
            fullPath = vuePath + do_main + ".vue";
        }
        System.out.println("fullPath = " + fullPath);
        FreemarkerUtil.generator(fullPath, param);
        System.out.println("------------- generateVue 结束 -------------\n");
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

    /**
     * 获取所有的Java类型，使用Set去重
     */
    private static Set<String> getJavaTypes(List<Field> fieldList) {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < fieldList.size(); i++) {
            Field field = fieldList.get(i);
            set.add(field.getJavaType());
        }
        return set;
    }
}
