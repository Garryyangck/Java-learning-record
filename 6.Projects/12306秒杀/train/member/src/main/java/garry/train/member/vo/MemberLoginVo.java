package garry.train.member.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * @author Garry
 * 2024-09-10 15:45
 */
@Data
public class MemberLoginVo {
    @JsonSerialize(using = ToStringSerializer.class) // 解决 Long 类型精度丢失的问题
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    private String mobile;

    /**
     * JWT token
     */
    private String token;
}
