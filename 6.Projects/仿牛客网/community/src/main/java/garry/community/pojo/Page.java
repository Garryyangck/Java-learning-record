package garry.community.pojo;

import lombok.Data;

/**
 * @author Garry
 * ---------2024/3/24 20:35
 **/
@Data
public class Page {
    private Integer pages;

    private Integer pageNum;

    private Integer prePage;

    private Integer nextPage;
}
