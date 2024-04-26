package garry.mall.service.impl;

import garry.mall.MallApplicationTest;
import garry.mall.vo.CategoryVo;
import garry.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Garry
 * ---------2024/3/8 17:32
 **/
@Slf4j
@Transactional
public class CategoryServiceImplTest extends MallApplicationTest {
    @Resource
    private CategoryServiceImpl categoryService;

    @Test
    public void selectAll() {
        ResponseVo<List<CategoryVo>> responseVo = categoryService.selectAll();
        log.info("responseVo = {}", responseVo);
    }

    @Test
    public void findSubCategoryId() {
        Set<Integer> resultSet = new HashSet<>();
        categoryService.findSubCategoryId(100001, resultSet);
        log.info("resultSet = {}", resultSet);
    }
}