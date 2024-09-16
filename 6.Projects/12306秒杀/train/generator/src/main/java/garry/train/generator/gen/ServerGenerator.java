package garry.train.generator.gen;

import garry.train.generator.util.FreemarkerUtil;

import java.util.HashMap;

public class ServerGenerator {
    private static String toPath = "generator/src/main/java/garry/train/generator/test/";

    public static void main(String[] args) throws Exception {
        FreemarkerUtil.initConfig("test.ftl");
        HashMap<String, Object> map = new HashMap<>();
        map.put("domain", "Test");
        FreemarkerUtil.generator(toPath + "Test.java", map);
    }
}
