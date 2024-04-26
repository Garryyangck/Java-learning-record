package garry.mall.service.impl;

import garry.mall.consts.MallConst;
import garry.mall.dao.CategoryMapper;
import garry.mall.enums.ResponseEnum;
import garry.mall.pojo.Category;
import garry.mall.service.ICategoryService;
import garry.mall.vo.CategoryVo;
import garry.mall.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Garry
 * ---------2024/3/8 17:05
 **/
@Service
public class CategoryServiceImpl implements ICategoryService {
    @Resource
    private CategoryMapper categoryMapper;

    /**
     * 耗时：http请求(外网) > mysql(内网+磁盘) > java(内存)
     */
    @Override
    public ResponseVo<List<CategoryVo>> selectAll() {
        List<Category> categories = categoryMapper.selectAll();

        //for循环找parentId=0的
//        List<CategoryVo> categoryVos = new ArrayList<>();
//        for (Category category : categories) {
//            if (category.getParentId().equals(MallConst.ROOT_PARENT_ID)) {
//                CategoryVo categoryVo = new CategoryVo();
//                BeanUtils.copyProperties(category, categoryVo);
//                categoryVos.add(categoryVo);
//            }
//        }

        /*使用Lambda表达式(参数 -> 方法体，方法体的return可以省略) + stream*/
        List<CategoryVo> categoryVoList = categories.stream()
                .filter(e -> e.getParentId().equals(MallConst.ROOT_PARENT_ID))
                .map(this::categoryToCategoryVo)
                .sorted(new Comparator<CategoryVo>() {
                    @Override
                    public int compare(CategoryVo o1, CategoryVo o2) {
                        return o2.getSortOrder() - o1.getSortOrder();
                    }
                })
                .collect(Collectors.toList());
        //为一级目录设置子目录
        findSubCategory(categoryVoList, categories);

        return ResponseVo.success(categoryVoList);
    }

    private CategoryVo categoryToCategoryVo(Category category) {
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category, categoryVo);
        return categoryVo;
    }

    //为指定categoryVoList set子目录
    private void findSubCategory(List<CategoryVo> categoryVoList/*需要set子目录的集合*/,
                                 List<Category> categories/*数据源*/) {
        for (CategoryVo categoryVo : categoryVoList) {
            //根据当前categoryVo的id从categories(数据源)中查找子目录
            List<CategoryVo> collect = categories.stream()
                    .filter(e -> e.getParentId().equals(categoryVo.getId()))
                    .map(this::categoryToCategoryVo)
                    .sorted(new Comparator<CategoryVo>() {
                        @Override
                        public int compare(CategoryVo o1, CategoryVo o2) {
                            return o2.getSortOrder() - o1.getSortOrder();
                        }
                    })
                    .collect(Collectors.toList());
            //查找出来的子目录也需要设置子目录
            findSubCategory(collect, categories);
            categoryVo.setSubCategories(collect);
        }
    }

    /**
     * 根据id查询其所有子类的id，在ProductService中会使用
     */
    @Override
    public void findSubCategoryId(Integer id, Set<Integer> resultSet) {
        List<Category> categories = categoryMapper.selectAll();
        findSubCategoryId(id, resultSet, categories);
    }

    public void findSubCategoryId(Integer id, Set<Integer> resultSet, List<Category> categories) {
        for (Category category : categories) {
            if (category.getParentId().equals(id)) {
                resultSet.add(category.getId());
                findSubCategoryId(category.getId(), resultSet, categories);
            }
        }
    }

    @Deprecated
    /*之前的做法，查询数据库的次数过多，分类数并不多，可以全部查出来再处理*/
    public ResponseVo<List<CategoryVo>> selectAllOld() {
        List<Category> parentCategories = categoryMapper.selectByParentId(MallConst.ROOT_PARENT_ID);
        ResponseVo<List<CategoryVo>> responseVo = new ResponseVo<>();
        responseVo.setStatus(ResponseEnum.SUCCESS.getStatus());
        List<CategoryVo> categoryVos = new ArrayList<>();

        for (Category parentCategory : parentCategories) {
            categoryVos.add(makeCategoryVo(parentCategory));
        }

        categoryVos.sort(new Comparator<CategoryVo>() {
            @Override
            public int compare(CategoryVo o1, CategoryVo o2) {
                return o2.getSortOrder() - o1.getSortOrder();
            }
        });
        responseVo.setData(categoryVos);
        return responseVo;
    }

    @Deprecated
    /*之前的做法，查询数据库的次数过多，分类数并不多，可以全部查出来再处理*/
    private CategoryVo makeCategoryVo(Category category) {
        CategoryVo categoryVo = new CategoryVo();

        categoryVo.setId(category.getId());
        categoryVo.setParentId(category.getParentId());
        categoryVo.setName(category.getName());
        categoryVo.setSortOrder(category.getSortOrder());
        List<CategoryVo> subCategoryVos = new ArrayList<>();
        categoryVo.setSubCategories(subCategoryVos);

        List<Category> subCategories = categoryMapper.selectByParentId(category.getId());
        if (subCategories == null) return categoryVo;

        for (Category subCategory : subCategories) {
            CategoryVo subCategoryVo = makeCategoryVo(subCategory);
            subCategoryVos.add(subCategoryVo);
        }

        categoryVo.getSubCategories().sort(new Comparator<CategoryVo>() {
            @Override
            public int compare(CategoryVo o1, CategoryVo o2) {
                return o2.getSortOrder() - o1.getSortOrder();
            }
        });
        return categoryVo;
    }
}
