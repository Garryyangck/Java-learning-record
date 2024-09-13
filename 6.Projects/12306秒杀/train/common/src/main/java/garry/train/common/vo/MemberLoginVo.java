package garry.train.common.vo;

import lombok.Data;

/**
 * @author Garry
 * 2024-09-10 15:45
 */
@Data
public class MemberLoginVo {
    private Long id;

    private String mobile;

    /**
     * JWT token
     */
    private String token;
}
