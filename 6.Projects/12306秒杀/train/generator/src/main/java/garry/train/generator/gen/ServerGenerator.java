package garry.train.generator.gen;

import lombok.Data;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.util.HashMap;

public class ServerGenerator {
    @Data
    private static class GeneratorResult {
        private String tableName;
        private String  domainObjectName;

        public GeneratorResult(String tableName, String domainObjectName) {
            this.tableName = tableName;
            this.domainObjectName = domainObjectName;
        }
    }

    private static String pomPath = "generator/pom.xml/";

    public static void main(String[] args) throws Exception {
        String generatorPath = getGeneratorPath();
        GeneratorResult generatorResult = getGeneratorResult(generatorPath);

    }

    private static GeneratorResult getGeneratorResult(String generatorPath) throws DocumentException {
        Document document = new SAXReader().read("generator/" + generatorPath);
        Node table = document.selectSingleNode("//table");
        System.out.println("table = " + table);
        Node tableName = table.selectSingleNode("@tableName");
        Node domainObjectName = table.selectSingleNode("@domainObjectName");
        System.out.println(tableName.getText() + "/" + domainObjectName.getText());
        return new GeneratorResult(tableName.getText(), domainObjectName.getText());
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
