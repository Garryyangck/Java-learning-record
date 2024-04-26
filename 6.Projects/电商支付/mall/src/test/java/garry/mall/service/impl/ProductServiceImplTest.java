package garry.mall.service.impl;

import com.github.pagehelper.PageInfo;
import garry.mall.MallApplicationTest;
import garry.mall.vo.ProductDetailVo;
import garry.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @author Garry
 * ---------2024/3/9 10:32
 **/
@Slf4j
public class ProductServiceImplTest extends MallApplicationTest {
    @Resource
    private ProductServiceImpl productService;

    @Test
    public void list() {
        ResponseVo<PageInfo> responseVo = productService.list(100001, 1, 2);
        log.info("responseVo = {}", responseVo);
        responseVo = productService.list(null, 1, 2);
        log.info("responseVo = {}", responseVo);
    }

    @Test
    public void detail() {
        ResponseVo<ProductDetailVo> responseVo = productService.detail(26);
        log.info("responseVo = {}", responseVo);
    }

}