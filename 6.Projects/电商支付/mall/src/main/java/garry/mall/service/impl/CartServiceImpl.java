package garry.mall.service.impl;

import com.google.gson.Gson;
import com.mysql.cj.util.StringUtils;
import garry.mall.consts.MallConst;
import garry.mall.dao.ProductMapper;
import garry.mall.enums.ProductStatusEnum;
import garry.mall.enums.ResponseEnum;
import garry.mall.form.CartAddForm;
import garry.mall.form.CartUpdateForm;
import garry.mall.pojo.Cart;
import garry.mall.pojo.Product;
import garry.mall.service.ICartService;
import garry.mall.vo.CartProductVo;
import garry.mall.vo.CartVo;
import garry.mall.vo.ResponseVo;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Garry
 * ---------2024/3/9 20:52
 **/
@Service
public class CartServiceImpl implements ICartService {
    private final Gson gson = new Gson();

    @Resource
    private ProductMapper productMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;//Spring给我们封装好的类

    @Override
    public ResponseVo<CartVo> list(Integer uid) {
        //根据uid获取redisKey
        String redisKey = String.format(MallConst.CART_REDIS_KEY_TEMPLATE, String.valueOf(uid));

        //拿到redisHash模板
        HashOperations<String, String, String>/*(redisKey,productId,json)*/
                opsForHash = stringRedisTemplate.opsForHash();

        //根据redisKey获取redis中对应的购物车(HashMap)
        Map<String, String> entries = opsForHash.entries(redisKey);

        //获取购物车的entrySet，用于遍历，entry<String/*productId*/,String/*json*/>
        //entry就是Map中的键值对的pair形式
        //entrySet相当于将Map中的键值对以集合的形式展示，遍历时可以直接取出一整个键值对遍历
        Set<Map.Entry<String/*productId*/, String/*json*/>> entrySet = entries.entrySet();

        //通过购物车中商品id的集合获取商品集合，sql不要在循环里，会严重影响性能
        //只查出来了status=1的商品，因此可能存在用户加入购物车时商品正在销售，
        //但是list时商品已经下架的情况，因此遍历时需要判断购物车的商品是否还存在
        //同时，还需要判断list时商品的库存是否足够，
        //因为可能存在加入购物车时库存足够，但是查看时库存不够的情况
        List<Product> productList = productMapper.selectByIdSet(
                entries.keySet().stream()/*只获取status=1的商品*/
                        .map(Integer::parseInt)//String -> Integer
                        .collect(Collectors.toSet()));

        //改进，直接以Map<Integer/*productId*/, Product>的方式存储，提升查询效率
        Map<Integer, Product> productMap = productList.stream()
                .collect(Collectors.toMap(product -> product.getId(), product -> product));

        CartVo cartVo = new CartVo();//返回信息
        List<CartProductVo> cartProductVoList = new ArrayList<>();//CartVo的属性

        for (Map.Entry<String, String> entry : entrySet) {
            Integer productId = Integer.parseInt(entry.getKey());
            Cart cart = gson.fromJson(entry.getValue(), Cart.class);
            Product product = productMap.get(productId);
            if (product != null) {
                //获取CartProductVo
                CartProductVo cartProductVo = makeCartProductVo(product, cart);

                //商品此时list的时候，库存已经比加入购物车时少了
                if (cartProductVo.getProductStock() < cartProductVo.getQuantity()) {
                    return ResponseVo.error(ResponseEnum.LACK_OF_PRODUCT, cartProductVo.getProductName() + " " + ResponseEnum.LACK_OF_PRODUCT.getMsg());
                }

                //增加到cartProductVoList
                cartProductVoList.add(cartProductVo);
                //只增加选中的商品的总数和总价
                if (cartProductVo.isProductSelected()) {
                    //增加购物车总数
                    cartVo.setCartTotalQuantity(cartVo.getCartTotalQuantity() + cartProductVo.getQuantity());
                    //增加购物车总价
                    cartVo.setCartTotalPrice(cartVo.getCartTotalPrice().add(cartProductVo.getProductTotalPrice()));
                }
                //设置是否全选(最开始默认全选)
                cartVo.setSelectAll(cartVo.isSelectAll() && cartProductVo.isProductSelected());
            } else {//购物车中存在商品已下架
                return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE, "id为" + cart.getProductId().toString() + " " + ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE.getMsg());
            }
        }

        cartVo.setCartProductVoList(cartProductVoList);
        return ResponseVo.success(cartVo);
    }

    @Override
    public ResponseVo<CartVo> add(Integer uid, CartAddForm cartAddForm) {
        //获取购物车id(redisKey)
        String redisKey = String.format(MallConst.CART_REDIS_KEY_TEMPLATE, String.valueOf(uid));
        Integer quantity = 1;

        Product product = productMapper.selectByPrimaryKey(cartAddForm.getProductId());

        //商品是否存在，且在售
        if (product == null || product.getStatus().equals(ProductStatusEnum.OFF_SALE.getStatus()) || product.getStatus().equals(ProductStatusEnum.DELETE.getStatus())) {
            return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE);
        }

        //库存是否充足
        if (product.getStock() <= 0) {
            return ResponseVo.error(ResponseEnum.LACK_OF_PRODUCT);
        }

        //拿到redisHash模板
        HashOperations<String, String, String>/*(redisKey,productId,json)*/
                opsForHash = stringRedisTemplate.opsForHash();

        //通过redisKey和key获取value
        String value = opsForHash.get(redisKey, String.valueOf(cartAddForm.getProductId()));

        Cart cart;
        if (StringUtils.isNullOrEmpty(value)) {
            //购物车没有该商品，新增
            cart = new Cart(cartAddForm.getProductId(), quantity, cartAddForm.isSelected());

        } else {
            //购物车有该商品，数量+quantity(1)
            cart = gson.fromJson(value, Cart.class);
            cart.setQuantity(cart.getQuantity() + quantity);
            cart.setProductSelected(cartAddForm.isSelected());
        }

        opsForHash.put(
                redisKey,//购物车名
                String.valueOf(cartAddForm.getProductId()),//商品id(key)
                gson.toJson(cart));//json字符串(value)

        //将购物车列表返回
        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> update(Integer uid, Integer productId, CartUpdateForm cartUpdateForm) {
        //根据uid获取redisKey
        String redisKey = String.format(MallConst.CART_REDIS_KEY_TEMPLATE, String.valueOf(uid));

        //拿到redisHash模板
        HashOperations<String, String, String>/*(redisKey,productId,json)*/
                opsForHash = stringRedisTemplate.opsForHash();

        //通过redisKey和key获取value
        String value = opsForHash.get(redisKey, String.valueOf(productId));

        Cart cart;
        if (StringUtils.isNullOrEmpty(value)) {
            //购物车没有该商品，报错
            return ResponseVo.error(ResponseEnum.PRODUCT_NOT_EXIST);

        } else {
            //购物车有该商品，更新
            cart = gson.fromJson(value, Cart.class);
            if (cartUpdateForm.getQuantity() != null && cartUpdateForm.getQuantity() >= 0)
                cart.setQuantity(cartUpdateForm.getQuantity());
            if (cartUpdateForm.getSelected() != null)
                cart.setProductSelected(cartUpdateForm.getSelected());
        }

        //更新数据库的内容
        opsForHash.put(
                redisKey,//购物车名
                String.valueOf(productId),//商品id(key)
                gson.toJson(cart));//json字符串(value)

        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> delete(Integer uid, Integer productId) {
        //根据uid获取redisKey
        String redisKey = String.format(MallConst.CART_REDIS_KEY_TEMPLATE, String.valueOf(uid));

        //拿到redisHash模板
        HashOperations<String, String, String>/*(redisKey,productId,json)*/
                opsForHash = stringRedisTemplate.opsForHash();

        //通过redisKey和key获取value
        String value = opsForHash.get(redisKey, String.valueOf(productId));

        //购物车没有该商品
        if (StringUtils.isNullOrEmpty(value)) {
            return ResponseVo.error(ResponseEnum.PRODUCT_NOT_EXIST);
        }

        //删除数据库中的指定元素
        opsForHash.delete(redisKey, String.valueOf(productId));

        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> selectAll(Integer uid) {
        String redisKey = String.format(MallConst.CART_REDIS_KEY_TEMPLATE, String.valueOf(uid));
        HashOperations<String, String, String>/*(redisKey,productId,json)*/
                opsForHash = stringRedisTemplate.opsForHash();

        //购物车的所有Cart集合
        List<Cart> cartList = listForCart(uid);

        for (Cart cart : cartList) {
            cart.setProductSelected(true);
            opsForHash.put(redisKey, String.valueOf(cart.getProductId()),
                    gson.toJson(cart));
        }

        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> unSelectAll(Integer uid) {
        String redisKey = String.format(MallConst.CART_REDIS_KEY_TEMPLATE, String.valueOf(uid));
        HashOperations<String, String, String>/*(redisKey,productId,json)*/
                opsForHash = stringRedisTemplate.opsForHash();

        //购物车的所有Cart集合
        List<Cart> cartList = listForCart(uid);

        for (Cart cart : cartList) {
            cart.setProductSelected(false);
            opsForHash.put(redisKey, String.valueOf(cart.getProductId()),
                    gson.toJson(cart));
        }

        return list(uid);
    }

    @Override
    public ResponseVo<Integer> sum(Integer uid) {
        String redisKey = String.format(MallConst.CART_REDIS_KEY_TEMPLATE, String.valueOf(uid));
        HashOperations<String, String, String>/*(redisKey,productId,json)*/
                opsForHash = stringRedisTemplate.opsForHash();

        //购物车的所有Cart集合
        List<Cart> cartList = listForCart(uid);

        /*int sum = 0;
        for (Cart cart : cartList) {
            sum += cart.getQuantity();
        }*/

        //使用stream+lambda表达式
        Integer sum = cartList.stream()
                .map(cart -> cart.getQuantity())
                .reduce(0, Integer::sum);

        return ResponseVo.success(sum);
    }

    /**
     * 根据Product和Cart获取CartProductVo、
     */
    private CartProductVo makeCartProductVo(Product product, Cart cart) {
        if (product == null || cart == null) return null;
        return new CartProductVo(
                product.getId(),
                cart.getQuantity(),
                product.getName(),
                product.getSubtitle(),
                product.getMainImage(),
                product.getPrice(),
                product.getStatus(),
                (product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity()))),
                product.getStock(),
                cart.isProductSelected());
    }

    /**
     * 将购物车中的所有Cart对象以List形式返回
     */
    private List<Cart> listForCart(Integer uid) {
        String redisKey = String.format(MallConst.CART_REDIS_KEY_TEMPLATE, String.valueOf(uid));
        HashOperations<String, String, String>/*(redisKey,productId,json)*/
                opsForHash = stringRedisTemplate.opsForHash();
        Map<String, String> entries = opsForHash.entries(redisKey);
        Set<Map.Entry<String/*productId*/, String/*json*/>> entrySet = entries.entrySet();

        List<Cart> cartList = new ArrayList<>();
        for (Map.Entry<String, String> entry : entrySet) {
            cartList.add(gson.fromJson(entry.getValue(), Cart.class));
        }

        return cartList;
    }
}
