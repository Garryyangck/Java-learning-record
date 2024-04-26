package garry.mall.controller;

import garry.mall.service.impl.CategoryServiceImpl;
import garry.mall.vo.CategoryVo;
import garry.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Garry
 * ---------2024/3/8 17:35
 **/
@SuppressWarnings("all")
@Slf4j
@RestController
public class CategoryController {
    @Resource
    private CategoryServiceImpl categoryService;

    /**
     * 多级目录查询
     *
     * @return
     */
    @GetMapping("/categories")
    public ResponseVo<List<CategoryVo>> categories() {
        return categoryService.selectAll();
    }
}
