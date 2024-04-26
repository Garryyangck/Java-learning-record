package garry.mall.service;

import garry.mall.vo.CategoryVo;
import garry.mall.vo.ResponseVo;

import java.util.List;
import java.util.Set;

/**
 * @author Garry
 * ---------2024/3/8 17:02
 **/
@SuppressWarnings("all")
public interface ICategoryService {
    /**
     * 查询所有
     *
     * @return
     */
    ResponseVo<List<CategoryVo>> selectAll();

    /**
     * 查询id分类的所有子类id
     *
     * @param id
     * @param resultSet
     */
    void findSubCategoryId(Integer id, Set<Integer> resultSet);
}
