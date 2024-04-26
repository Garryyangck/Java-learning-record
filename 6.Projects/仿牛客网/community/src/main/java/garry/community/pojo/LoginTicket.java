package garry.community.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author Garry
 * ---------2024/3/25 18:35
 **/
@Data
public class LoginTicket {
    private Integer userId;

    private String ticket;

    private Integer status;

    private Date expired;
}
