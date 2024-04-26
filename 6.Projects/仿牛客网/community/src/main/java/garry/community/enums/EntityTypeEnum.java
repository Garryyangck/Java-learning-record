package garry.community.enums;

import lombok.Getter;

/**
 * @author Garry
 * ---------2024/3/21 13:12
 **/

/**
 * 对象实体枚举(比如帖子，或别人的评论)
 */
@Getter
public enum EntityTypeEnum {

    DISCUSS_POST(1, "帖子"),

    COMMENT(2, "评论"),

    USER(3, "用户"),

    ;

    private final Integer code;//对象实体编号

    private final String desc;//详情

    EntityTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
