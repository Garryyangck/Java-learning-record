package garry.community.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Message {
    private Integer id;

    private Integer fromId;

    private Integer toId;

    private String conversationId;

    private String content;

    private Integer status;

    private Date createTime;
}