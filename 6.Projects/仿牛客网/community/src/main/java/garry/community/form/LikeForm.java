package garry.community.form;

import lombok.Data;

/**
 * @author Garry
 * ---------2024/3/24 15:12
 **/
@Data
public class LikeForm {
    Integer entityType;

    Integer entityId;

    Integer receiverId;

    Integer postId;
}
