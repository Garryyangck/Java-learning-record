package garry.community.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Comment {
    private Integer id;

    private Integer userId;

    private Integer entityType;

    private Integer entityId;

    private Integer targetId;

    private String content;

    private Integer status;

    private Date createTime;
}