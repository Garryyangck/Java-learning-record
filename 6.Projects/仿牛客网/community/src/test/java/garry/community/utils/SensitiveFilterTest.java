package garry.community.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import garry.community.CommunityApplicationTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Garry
 * ---------2024/3/20 15:27
 **/
@Slf4j
public class SensitiveFilterTest extends CommunityApplicationTests {
    @Resource
    private SensitiveFilter sensitiveFilter;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void filter() {
        List<String> list = sensitiveFilter.showKeywords();
        log.info("前缀树的所有敏感词 = {}", gson.toJson(list));
    }

    @Test
    public void showKeywords() {
        String filter = "";
        filter = sensitiveFilter.filter("我.是.一.条.很.长.的.敏.感.词");
        log.info("过滤后的结果 = {}", gson.toJson(filter));
        //过滤后的结果 = "***"
        filter = sensitiveFilter.filter("我的祖国是中国");
        log.info("过滤后的结果 = {}", gson.toJson(filter));
        //过滤后的结果 = "我的祖国是***"
        filter = sensitiveFilter.filter("我们今天来聊一聊政治");
        log.info("过滤后的结果 = {}", gson.toJson(filter));
        //过滤后的结果 = "我们今天来聊一聊***"
        filter = sensitiveFilter.filter("中国和政治和中国政治");
        log.info("过滤后的结果 = {}", gson.toJson(filter));
        //过滤后的结果 = "***和***和***"
        filter = sensitiveFilter.filter("你这个(❁´◡`❁)出(❁´◡`❁)生(❁´◡`❁)东西");
        log.info("过滤后的结果 = {}", gson.toJson(filter));
        //过滤后的结果 = "你这个(❁´◡`❁)***(❁´◡`❁)东西"
    }
}