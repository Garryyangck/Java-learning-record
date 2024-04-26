package garry.community;

import garry.community.dao.DiscussPostMapper;
import garry.community.enums.DiscussPostStatusEnum;
import garry.community.enums.DiscussPostTypeEnum;
import garry.community.pojo.DiscussPost;
import garry.community.service.DiscussPostService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author Garry
 * ---------2024/3/31 14:58
 **/
@Slf4j
public class CaffeineTest extends CommunityApplicationTests {
    @Resource
    private DiscussPostService discussPostService;

    @Resource
    private DiscussPostMapper discussPostMapper;

    @Test
    public void initForTest() {
        for (int i = 0; i < 100000; i++) {
            DiscussPost post = new DiscussPost();
            post.setUserId(100000);
            post.setTitle("压力测试帖子(" + i + ")");
            post.setContent("我是用于压力测试的帖子");
            post.setType(DiscussPostTypeEnum.NORMAL.getCode());
            post.setStatus(DiscussPostStatusEnum.NORMAL.getCode());
            post.setCreateTime(new Date(System.currentTimeMillis() - 1000L * 3600L * 24L * 365L * 6L));
            post.setCommentCount(0);
            post.setScore(0.0);
            discussPostService.addDiscussPost(post);
        }
    }

    @Test
    public void deleteTestPosts() {
        discussPostMapper.deleteByUserId(100000);
    }

    @Test
    public void testCache() {
        /*
            总用时：223ms
         */
        discussPostService.findHotDiscussPosts(0, 1, 10, true);//第一次从数据库中初始化用时104ms
        discussPostService.findHotDiscussPosts(0, 1, 10, true);//后面从本地缓存中获取数据用时0ms
        discussPostService.findHotDiscussPosts(0, 1, 10, true);//0ms
        discussPostService.findHotDiscussPosts(0, 1, 10, true);//0ms
    }

    @Test
    public void testMapper() {
        /*
            总用时：339ms
         */
        discussPostService.findHotDiscussPosts(0, 1, 10, true);//93ms
        discussPostService.findHotDiscussPosts(0, 1, 10, true);//37ms
        discussPostService.findHotDiscussPosts(0, 1, 10, true);//38ms
        discussPostService.findHotDiscussPosts(0, 1, 10, true);//39ms
    }

    /*
        尝试扩大增加查询的次数，100000条数据，我们查找10000次不过分吧
     */
    @Test
    public void testCache10000Times() {
        /*
            总用时：1245ms(1s 245ms)
         */
        for (int i = 0; i < 10000; i++) {
            discussPostService.findHotDiscussPosts(0, 1, 10, true);
        }
    }

    @Test
    public void testMapper10000Times() {
        /*
            总用时：361903ms(6m 1s 903ms)
            本地缓存后的效率相比缓存前提升290倍
         */
        for (int i = 0; i < 10000; i++) {
            discussPostService.findHotDiscussPosts(0, 1, 10, true);
        }
    }

    /*
        使用专业测试工具jmeter进行压力测试
     */

    /*
        使用jmeter进行压力测试(50线程，访问热门帖子，访问间隔0~2000ms之间随机数，总访问时间60s)
        测试结果：
        1)使用本地缓存：吞吐量: 42.6/sec
        2)不使用本地缓存：吞吐量: 40.1/sec
     */

    /*
        使用jmeter进行压力测试(80线程，访问热门帖子，访问间隔0~1500ms之间随机数，总访问时间60s)
        测试结果：
        1)使用本地缓存：吞吐量: 85.8/sec
        2)不使用本地缓存：吞吐量: 81.0/sec
     */

    /*
        使用jmeter进行压力测试(100线程，访问热门帖子，访问间隔0~1000ms之间随机数，总访问时间60s)
        测试结果：
        1)使用本地缓存：吞吐量: 147.8/sec(CPU 40%)
        2)不使用本地缓存：吞吐量: 129.6/sec(CPU 100%, 瓶颈)
     */

    /*
        使用jmeter进行压力测试(120线程，访问热门帖子，访问间隔0~800ms之间随机数，总访问时间30s)
        测试结果：
        1)使用本地缓存：吞吐量: 204.5/sec(CPU 55~60%)
        2)不使用本地缓存：吞吐量: 142.3/sec(CPU 100%, 瓶颈)
     */

    /*
        使用jmeter进行压力测试(150线程，访问热门帖子，访问间隔0~750ms之间随机数，总访问时间15s)
        测试结果：
        1)使用本地缓存：吞吐量: 260.3/sec(CPU 70%)
        2)不使用本地缓存：吞吐量: 137.2/sec(CPU 100%, 瓶颈)
     */

    /*
        使用jmeter进行压力测试(175线程，访问热门帖子，访问间隔0~600ms之间随机数，总访问时间12s)
        测试结果：
        1)使用本地缓存：吞吐量: 338.0/sec(CPU 85%)
        2)不使用本地缓存：吞吐量: 136.4/sec(CPU 100%, 瓶颈)
     */

    /*
        使用jmeter进行压力测试(200线程，访问热门帖子，访问间隔0~500ms之间随机数，总访问时间10s)
        测试结果：
        1)使用本地缓存：吞吐量: 428.3/sec(CPU 100%, 瓶颈)
        2)不使用本地缓存：吞吐量: 134.5/sec(CPU 100%, 瓶颈)
     */
}
