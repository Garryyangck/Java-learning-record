package garry.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import garry.mall.dao.ShippingMapper;
import garry.mall.enums.ResponseEnum;
import garry.mall.form.ShippingForm;
import garry.mall.pojo.Shipping;
import garry.mall.service.IShippingService;
import garry.mall.vo.ResponseVo;
import garry.mall.vo.ShippingVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Garry
 * ---------2024/3/10 19:27
 **/
@Service
public class ShippingServiceImpl implements IShippingService {
    @Resource
    private ShippingMapper shippingMapper;

    @Override
    public ResponseVo<Map<String, Integer>> add(Integer uid, ShippingForm shippingForm) {
        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(shippingForm, shipping);
        shipping.setUserId(uid);

        int count/*影响行数*/ = shippingMapper.insertSelective(shipping);

        if (count == 0) {
            return ResponseVo.error(ResponseEnum.ERROR, "新建地址失败");
        }

        HashMap<String, Integer> map = new HashMap<>();
        map.put("shippingId", shipping.getId());
        return ResponseVo.success(map);
    }

    @Override
    public ResponseVo delete(Integer uid, Integer shippingId) {
        int count = shippingMapper.deleteByIdAndUserId(shippingId, uid);

        if (count == 0) {
            return ResponseVo.error(ResponseEnum.ERROR, "删除地址失败");
        }

        return ResponseVo.success();
    }

    @Override
    public ResponseVo update(Integer uid, Integer shippingId, ShippingForm shippingForm) {
        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(shippingForm, shipping);
        shipping.setUserId(uid);
        shipping.setId(shippingId);

        int count = shippingMapper.updateByPrimaryKeySelective(shipping);

        if (count == 0) {
            return ResponseVo.error(ResponseEnum.ERROR, "更新地址失败");
        }

        return ResponseVo.success();
    }

    @Override
    public ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize) {
        List<Shipping> shippingList = shippingMapper.selectByUserId(uid);

        PageHelper.startPage(pageNum, pageSize);

        List<ShippingVo> shippingVoList = shippingList.stream()
                .map(shipping -> {
                    ShippingVo shippingVo = new ShippingVo();
                    BeanUtils.copyProperties(shipping, shippingVo);
                    return shippingVo;
                }).collect(Collectors.toList());

        PageInfo pageInfo = new PageInfo(shippingList);
        pageInfo.setList(shippingVoList);
        return ResponseVo.success(pageInfo);
    }
}
