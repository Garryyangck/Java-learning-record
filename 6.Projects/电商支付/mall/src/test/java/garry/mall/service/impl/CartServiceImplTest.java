package garry.mall.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import garry.mall.MallApplicationTest;
import garry.mall.form.CartAddForm;
import garry.mall.form.CartUpdateForm;
import garry.mall.vo.CartVo;
import garry.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @author Garry
 * ---------2024/3/9 21:59
 **/
@Slf4j
public class CartServiceImplTest extends MallApplicationTest {
    @Resource
    private CartServiceImpl cartService;

    //测试时，将log.info中的Object以json的格式输出在控制台上
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private final Integer UID = 1;

    private final Integer PRODUCT_ID = 26;

    private final Integer WRONG_PRODUCT_ID = 666;

    @Before//每个测试在执行前都会先执行该测试
    public void add() {
        log.info("【新增购物车...】");

        CartAddForm cartAddForm = new CartAddForm();//默认selected为true
        ResponseVo<CartVo> responseVo;

        cartAddForm.setProductId(PRODUCT_ID);
        cartAddForm.setSelected(true);
        responseVo = cartService.add(UID, cartAddForm);
        log.info("responseVo = {}", gson.toJson(responseVo));

        cartAddForm.setProductId(WRONG_PRODUCT_ID);
        cartAddForm.setSelected(true);
        responseVo = cartService.add(UID, cartAddForm);
        log.info("responseVo = {}", gson.toJson(responseVo));
    }

    @Test
    public void list() {
        log.info("【查看购物车...】");

        ResponseVo<CartVo> responseVo = cartService.list(UID);
        log.info("responseVo = {}", gson.toJson(responseVo));
    }

    @Test
    public void update() {
        log.info("【更新购物车...】");

        CartUpdateForm cartUpdateForm = new CartUpdateForm();

        cartUpdateForm.setQuantity(5);
        cartUpdateForm.setSelected(true);
        ResponseVo<CartVo> responseVo = cartService.update(UID, PRODUCT_ID, cartUpdateForm);
        log.info("responseVo = {}", gson.toJson(responseVo));

        cartUpdateForm.setQuantity(null);
        cartUpdateForm.setSelected(null);
        responseVo = cartService.update(UID, PRODUCT_ID, cartUpdateForm);
        log.info("responseVo = {}", gson.toJson(responseVo));

        cartUpdateForm.setQuantity(5);
        cartUpdateForm.setSelected(true);
        responseVo = cartService.update(UID, WRONG_PRODUCT_ID, cartUpdateForm);
        log.info("responseVo = {}", gson.toJson(responseVo));
    }

    @After//每个测试在结束后都会执行此测试
    public void delete() {
        log.info("【删除购物车...】");

        ResponseVo<CartVo> responseVo = cartService.delete(UID, PRODUCT_ID);
        log.info("responseVo = {}", gson.toJson(responseVo));

        responseVo = cartService.delete(UID, WRONG_PRODUCT_ID);
        log.info("responseVo = {}", gson.toJson(responseVo));
    }

    @Test
    public void selectAll() {
        log.info("【全选购物车...】");

        ResponseVo<CartVo> responseVo = cartService.selectAll(UID);
        log.info("responseVo = {}", gson.toJson(responseVo));
    }

    @Test
    public void unSelectAll() {
        log.info("【全不选购物车...】");

        ResponseVo<CartVo> responseVo = cartService.unSelectAll(UID);
        log.info("responseVo = {}", gson.toJson(responseVo));
    }

    @Test
    public void sum() {
        log.info("【购物车商品总数...】");

        ResponseVo<Integer> responseVo = cartService.sum(UID);
        log.info("responseVo = {}", gson.toJson(responseVo));
    }
}