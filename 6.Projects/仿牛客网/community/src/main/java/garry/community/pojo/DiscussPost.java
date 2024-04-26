package garry.community.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

//在ES搜索的实体上进行配置
@Document(indexName = "discuss_post", type = "_doc", shards = 6, replicas = 3)
@Data
public class DiscussPost {
    @Id
    private Integer id;

    @Field(type = FieldType.Integer)
    private Integer userId;

    //存储时的分词器analyzer，使用ik_max_word分词器，拆分成最多的词；
    //搜索时的分词器searchAnalyzer，使用ik_smart分词器，能揣测你的意图分词
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String title;

    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String content;

    @Field(type = FieldType.Integer)
    private Integer type;

    @Field(type = FieldType.Integer)
    private Integer status;

    @Field(type = FieldType.Date)
    private Date createTime;

    @Field(type = FieldType.Integer)
    private Integer commentCount;

    @Field(type = FieldType.Double)
    private Double score;
}