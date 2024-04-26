package garry.mall.service.impl;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import garry.mall.MallApplicationTest;
import garry.mall.form.CartAddForm;
import garry.mall.vo.OrderVo;
import garry.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Random;

/**
 * @author Garry
 * ---------2024/3/11 14:07
 **/
@Slf4j
public class OrderServiceImplTest extends MallApplicationTest {
    @Resource
    private OrderServiceImpl orderService;

    @Resource
    private CartServiceImpl cartService;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private final Integer UID = 1;

    @Test
    public void add() {
        CartAddForm cartAddForm = new CartAddForm();//默认selected为true

        for (int i = 0; i < 4; i++) {
            int random = new Random().nextInt(5);
            for (int j = 0; j < random; j++) {
                cartAddForm.setProductId(26 + i);
                cartAddForm.setSelected(true);
                cartService.add(UID, cartAddForm);
            }
        }
    }

    @Test
    public void create() {
        ResponseVo<OrderVo> responseVo = orderService.create(UID, 10);
        log.info("responseVo = {}", gson.toJson(responseVo));
    }

    @Test
    public void list() {
        ResponseVo<PageInfo> responseVo = orderService.list(UID, 1, 10);
        log.info("responseVo = {}", gson.toJson(responseVo));
    }

    @Test
    public void detail() {
        ResponseVo<OrderVo> responseVo = orderService.detail(UID, 1710212823525L);
        log.info("responseVo = {}", gson.toJson(responseVo));
    }

    @Test
    public void cancel() {
        ResponseVo responseVo = orderService.cancel(UID, 1710224883568L);
        log.info("responseVo = {}", gson.toJson(responseVo));
    }
}