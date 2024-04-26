package garry.mall.controller;

import com.github.pagehelper.PageInfo;
import garry.mall.service.impl.ProductServiceImpl;
import garry.mall.vo.ProductDetailVo;
import garry.mall.vo.ResponseVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Garry
 * ---------2024/3/9 12:05
 **/
@SuppressWarnings("all")
@RestController
public class ProductController {
    @Resource
    private ProductServiceImpl productService;

    /**
     * 根据categoryId，进行商品列表查询
     *
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/products")
    //注意这里参数获取的方式不是json，而是直接获取基本数据类型参数
    public ResponseVo<PageInfo> list(@RequestParam(required = false) Integer categoryId,
                                     @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                     @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return productService.list(categoryId, pageNum, pageSize);
    }

    /**
     * 查询productId的商品详情
     *
     * @param productId
     * @return
     */
    @GetMapping("/products/{productId}")
    public ResponseVo<ProductDetailVo> detail(@PathVariable(value = "productId") Integer productId) {
        return productService.detail(productId);
    }
}
