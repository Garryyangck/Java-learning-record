package garry.train.common.util;

import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author Garry
 * 2024-09-30 21:25
 */
public class PageUtil {

    /**
     * 根据已有的 list，创建 pageInfo 对象
     */
    public static <T> PageInfo<T> getPageInfo(List<T> list, int pageNum, int pageSize) {
        int total = list.size(); // 总记录数
        int flag = total - total / pageSize * pageSize;
        int lastPageSize = flag == 0 ? pageSize : flag; // 最后一页的条目数
        int pages = total / pageSize + (flag == 0 ? 0 : 1); // 总页数
        int size = pageNum == pages ? lastPageSize : pageSize; // 当前页的记录数
        int startRow = (pageNum - 1) * pageSize; // 当前页的起始行
        int endRow = startRow + pageSize - 1; // 当前页的结束行

        PageInfo<T> pageInfo = new PageInfo<>(list);
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        pageInfo.setTotal(total);
        pageInfo.setPages(pages);
        pageInfo.setSize(size);
        pageInfo.setStartRow(startRow);
        pageInfo.setEndRow(endRow);

        return pageInfo;
    }
}
