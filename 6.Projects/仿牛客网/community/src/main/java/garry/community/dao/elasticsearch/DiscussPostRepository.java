package garry.community.dao.elasticsearch;

import garry.community.pojo.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Garry
 * ---------2024/3/27 19:50
 **/
/*这里使用Repository，Mapper是Mybatis独有的注解*/
@Repository
public interface DiscussPostRepository
        extends ElasticsearchRepository<DiscussPost/*搜索的类型*/, Integer/*主键的类型*/> {
            /*不需要写任何方法，继承的父类中已经有增删改查所需的方法*/
}
