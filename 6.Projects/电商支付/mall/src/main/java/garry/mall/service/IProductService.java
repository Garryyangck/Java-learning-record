package garry.mall.service;

import com.github.pagehelper.PageInfo;
import garry.mall.vo.ProductDetailVo;
import garry.mall.vo.ResponseVo;

/**
 * @author Garry
 * ---------2024/3/9 9:56
 **/
@SuppressWarnings("all")
public interface IProductService {

    /**
     * 通过categoryId获取在该目录下的具体商品列表（包括其子目录）
     *
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @return
     */
    ResponseVo<PageInfo> list(Integer categoryId, Integer pageNum, Integer pageSize);

    /**
     * 获取商品详情
     *
     * @param productId
     * @return
     */
    ResponseVo<ProductDetailVo> detail(Integer productId);
}
