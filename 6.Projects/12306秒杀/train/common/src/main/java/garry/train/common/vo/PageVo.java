package garry.train.common.vo;

import com.github.pagehelper.PageInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Garry
 * 2024-09-15 16:59
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PageVo<T> extends PageInfo<T> {
    /**
     * 后端额外携带的分页信息
     */
    private String msg;
}
