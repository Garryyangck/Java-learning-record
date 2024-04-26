package garry.community.quartz;

import garry.community.enums.DiscussPostStatusEnum;
import garry.community.enums.EntityTypeEnum;
import garry.community.pojo.DiscussPost;
import garry.community.service.DiscussPostService;
import garry.community.service.ElasticsearchService;
import garry.community.service.LikeService;
import garry.community.utils.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Garry
 * ---------2024/3/30 14:30
 **/
@Slf4j
public class PostScoreRefreshJob implements Job {
    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private DiscussPostService discussPostService;

    @Resource
    private LikeService likeService;

    @Resource//分数变了，搜索引擎的数据也要改变
    private ElasticsearchService elasticsearchService;

    private static final Date birthDay;

    static {
        try {
            birthDay = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2004-01-27 23:30:17");
        } catch (ParseException e) {
            throw new RuntimeException("【初始化时间失败】", e);
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String redisKey = RedisKeyUtil.getPostKey();
        BoundSetOperations operations = redisTemplate.boundSetOps(redisKey);

        if (operations.size() <= 0) {
            log.info("【没有需要刷新的帖子】");
            return;
        }

        long begin = System.currentTimeMillis();

        Long size = operations.size();
        while (operations.size() > 0) {
            this.refresh((Integer) operations.pop());
        }

        long end = System.currentTimeMillis();

        log.info("【帖子刷新用时:{}, 刷新数目:{}】", end - begin, size);
    }

    public void refresh(Integer postId) {
        DiscussPost post = discussPostService.findPostById(postId);
        if (post == null) {
            log.error("【该帖子不存在，id = {}】", postId);
            return;
        }

        //是否精华
        boolean isWonderful = post.getStatus().equals(DiscussPostStatusEnum.WONDERFUL.getCode());
        //评论数目
        Integer commentCount = post.getCommentCount();
        //点赞数目
        long likeCount = likeService.findEntityLikeCount(EntityTypeEnum.DISCUSS_POST.getCode(), postId);

        //计算权重
        double w = (isWonderful ? 500 : 0) + commentCount * 10 + likeCount * 3;

        //计算分数
        double score = (Math.log(Math.max(1, w))) / (Math.log(2))
                + (post.getCreateTime().getTime() - birthDay.getTime()) / (1000 * 3600 * 24);
        post.setScore(score);
        //更新数据库
        int rows = discussPostService.update(post);
        if (rows <= 0) {
            throw new RuntimeException("【分数更新失败】");
        }

        //同步搜索数据
        elasticsearchService.addPost(post);
    }
}
