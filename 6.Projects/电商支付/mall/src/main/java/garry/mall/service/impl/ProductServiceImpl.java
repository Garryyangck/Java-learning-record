package garry.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import garry.mall.dao.ProductMapper;
import garry.mall.enums.ProductStatusEnum;
import garry.mall.enums.ResponseEnum;
import garry.mall.pojo.Product;
import garry.mall.service.IProductService;
import garry.mall.vo.ProductDetailVo;
import garry.mall.vo.ProductVo;
import garry.mall.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Garry
 * ---------2024/3/9 9:59
 **/
@Service
public class ProductServiceImpl implements IProductService {
    @Resource
    private CategoryServiceImpl categoryService;

    @Resource
    private ProductMapper productMapper;

    @Override
    public ResponseVo<PageInfo> list(Integer categoryId, Integer pageNum, Integer pageSize) {
        Set<Integer> categoryIdSet = new HashSet<>();
        if (categoryId != null) {
            categoryService.findSubCategoryId(categoryId, categoryIdSet);//获取子目录id
            categoryIdSet.add(categoryId);//将自身id添加
        }//categoryId == null，则categoryIdSet.size() == 0，

        PageHelper.startPage(pageNum, pageSize);//是的，就只用一行代码...

        List<Product> productList = productMapper.selectByCategoryIdSet(categoryIdSet);//获取在这些目录里下的商品
        List<ProductVo> productVoList = productList.stream().map(product -> {
            ProductVo productVo = new ProductVo();
            BeanUtils.copyProperties(product, productVo);
            return productVo;
        }).collect(Collectors.toList());

        //获取分页详情
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productVoList);
        return ResponseVo.success(pageInfo);
    }

    @Override
    public ResponseVo<ProductDetailVo> detail(Integer productId) {
        Product product = productMapper.selectByPrimaryKey(productId);

        //只对针对性条件判断
        if (product == null || product.getStatus().equals(ProductStatusEnum.OFF_SALE.getStatus()) || product.getStatus().equals(ProductStatusEnum.DELETE.getStatus())) {
            return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE);
        }

        //敏感数据处理
        product.setStock(product.getStock() > 100 ? 100 : product.getStock());
        ProductDetailVo productDetailVo = new ProductDetailVo();
        BeanUtils.copyProperties(product, productDetailVo);
        return ResponseVo.success(productDetailVo);
    }
}
