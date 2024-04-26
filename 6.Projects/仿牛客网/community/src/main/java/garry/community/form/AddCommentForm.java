package garry.community.form;

import lombok.Data;

/**
 * @author Garry
 * ---------2024/3/21 20:01
 **/
@Data
public class AddCommentForm {
    private Integer postId;

    private Integer entityType;

    private Integer entityId;

    private Integer targetId;

    private String content;
}
