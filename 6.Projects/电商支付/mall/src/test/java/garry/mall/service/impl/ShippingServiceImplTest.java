package garry.mall.service.impl;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import garry.mall.MallApplicationTest;
import garry.mall.form.ShippingForm;
import garry.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Garry
 * ---------2024/3/10 19:52
 **/
@Slf4j
public class ShippingServiceImplTest extends MallApplicationTest {
    @Resource
    private ShippingServiceImpl shippingService;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private final Integer UID = 1;

    @Test
    public void add() {
        ShippingForm shippingForm = new ShippingForm();
        shippingForm.setReceiverName("Garry");
        shippingForm.setReceiverAddress("BIT");
        shippingForm.setReceiverMobile("16666666666");
        shippingForm.setReceiverPhone("010");
        shippingForm.setReceiverProvince("北京");
        shippingForm.setReceiverDistrict("房山区");
        shippingForm.setReceiverCity("北京");
        shippingForm.setReceiverZip("00000");
        ResponseVo<Map<String, Integer>> responseVo = shippingService.add(UID, shippingForm);
        log.info("responseVo = {}", responseVo);
    }

    @Test
    public void delete() {
        ResponseVo responseVo = shippingService.delete(UID, 9);
        log.info("responseVo = {}", responseVo);
    }

    @Test
    public void update() {
        ShippingForm shippingForm = new ShippingForm();
        shippingForm.setReceiverName("yck");
        shippingForm.setReceiverAddress("BIT");
        shippingForm.setReceiverMobile("16666666666");
        shippingForm.setReceiverPhone("010");
        shippingForm.setReceiverProvince("北京");
        shippingForm.setReceiverDistrict("房山区");
        shippingForm.setReceiverCity("北京");
        shippingForm.setReceiverZip("00000");
        ResponseVo responseVo = shippingService.update(UID, 10, shippingForm);
        log.info("responseVo = {}", responseVo);
    }

    @Test
    public void list() {
        ResponseVo<PageInfo> responseVo = shippingService.list(1, 1, 10);
        log.info("responseVo = {}", gson.toJson(responseVo));
    }
}