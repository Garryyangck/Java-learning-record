package garry.mall.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Garry
 * ---------2024/3/8 16:59
 **/

/**
 * 多级目录查询
 */
@Data
public class CategoryVo {
    private Integer id;

    private Integer parentId;

    private String name;

    private Integer sortOrder;

    private List<CategoryVo> subCategories;
}
