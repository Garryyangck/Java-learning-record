package garry.train.common.util;

import com.github.pagehelper.PageInfo;
import garry.train.common.consts.CommonConst;

import java.util.List;

/**
 * @author Garry
 * 2024-09-30 21:25
 */
public class PageUtil {

    /**
     * 根据已有的 list，创建 pageInfo 对象
     *
     * @param list          已完成查找后的集合
     * @param pageNum       当前页
     * @param pageSize      每页的数量
     * @param navigatePages 导航页长度，假如为 4，则左右各 2 页
     */
    public static <T> PageInfo<T> getPageInfo(List<T> list, int pageNum, int pageSize, int navigatePages) {
        int total = list.size(); // 总记录数
        int flag = total - total / pageSize * pageSize; // 虚拟最后一页，即页数为 0~(pageSize-1)
        int lastPageSize = flag == 0 ? pageSize : flag; // 最后一页的条目数，页数为 1~pageSize
        int pages = total / pageSize + (flag == 0 ? 0 : 1); // 总页数
        int size = (pageNum == pages || pages == 0) ? lastPageSize : pageSize; // 当前页的记录数
        int startRow = (pageNum - 1) * pageSize; // 当前页的起始行
        int endRow = startRow + Math.min(size, pageSize) - 1; // 当前页的结束行

        PageInfo<T> pageInfo = new PageInfo<>(list);
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        pageInfo.setTotal(total);
        pageInfo.setPages(pages);
        pageInfo.setSize(size);
        pageInfo.setStartRow(startRow);
        pageInfo.setEndRow(endRow);

        // 设置 navigatePages，navigatepageNums，navigateFirstPage，navigateLastPage，
        // prePage，nextPage，isFirstPage，isLastPage，hasPreviousPage，hasNextPage
        pageInfo.calcByNavigatePages(navigatePages);

        return pageInfo;
    }

    /**
     * 根据已有的 list，创建 pageInfo 对象
     * 默认 navigatePages 为 CommonConst.DEFAULT_NAVIGATE_PAGES
     *
     * @param list     已完成查找后的集合
     * @param pageNum  当前页
     * @param pageSize 每页的数量
     */
    public static <T> PageInfo<T> getPageInfo(List<T> list, int pageNum, int pageSize) {
        return getPageInfo(list, pageNum, pageSize, CommonConst.DEFAULT_NAVIGATE_PAGES);
    }
}
