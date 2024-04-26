package garry.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import garry.mall.consts.MallConst;
import garry.mall.dao.OrderItemMapper;
import garry.mall.dao.OrderMapper;
import garry.mall.dao.ProductMapper;
import garry.mall.dao.ShippingMapper;
import garry.mall.enums.OrderStatusEnum;
import garry.mall.enums.PaymentTypeEnum;
import garry.mall.enums.ResponseEnum;
import garry.mall.pojo.*;
import garry.mall.service.IOrderService;
import garry.mall.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Garry
 * ---------2024/3/11 10:31
 **/
@Service
public class OrderServiceImpl implements IOrderService {
    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderItemMapper orderItemMapper;

    @Resource
    private CartServiceImpl cartService;

    @Resource
    private ShippingMapper shippingMapper;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private final Gson gson = new Gson();

    @Override
    @Transactional//开启事务，本方法中所有DML处在同一个事务下
    public ResponseVo<OrderVo> create(Integer uid, Integer shippingId) {
        Long orderNo = generateOrderNo();//随机生成订单号
        Integer postage = 0;//运费默认为0

        /*
            1.收货地址校验（总之要查出来的）
         */
        Shipping shipping = shippingMapper.selectByIdAndUserId(shippingId, uid);

        //收货地址不存在
        if (shipping == null) {
            return ResponseVo.error(ResponseEnum.SHIPPING_NOT_EXIST);
        }
        ShippingVo shippingVo = new ShippingVo();
        BeanUtils.copyProperties(shipping, shippingVo);

        /*
            2.获取购物车，校验（是否有商品、库存）
         */

        //使用list方法获取当前redis中uid用户的购物车的相关信息
        //list方法内部是根据当前redis中Cart的productId查找了数据库，
        //因此其返回的product相关的数据都是最新的
        //此外，list中会由于此刻购物车中商品已下架或商品库存不足返回error的ResponseVo，
        //因此要优先判断
        ResponseVo<CartVo> responseVoByList = cartService.list(uid);

        //判断ResponseVo的种类，如果不是成功，直接将其错误信息返回（校验是否有商品、库存）
        if (!responseVoByList.getStatus().equals(ResponseEnum.SUCCESS.getStatus())) {
            return new ResponseVo<>(responseVoByList.getStatus(), responseVoByList.getMsg());
        }

        //获取成功的responseVoByList的data
        CartVo cartVo = responseVoByList.getData();

        //购物车没有选中的商品
        if (cartVo.getCartTotalQuantity() == 0) {
            return ResponseVo.error(ResponseEnum.CART_EMPTY);
        }

        //从CartVo中获取购物车中每件商品的详情
        List<CartProductVo> cartProductVoList = cartVo.getCartProductVoList();

        //OrderItem集合，先不要急着添加数据库，要和Order一起以事务的方式加入数据库
        List<OrderItem> orderItemList = new ArrayList<>();

        //OrderVo的OrderVo的orderItemVoList字段
        List<OrderItemVo> orderItemVoList = new ArrayList<>();

        //遍历购物车中的每件商品
        for (CartProductVo cartProductVo : cartProductVoList) {
            //必须是选中的商品，才会加入订单
            if (cartProductVo.isProductSelected()) {
                //创建orderItem，userId和orderNo没有拷贝过来
                OrderItem orderItem = cartProductVoToOrderItem(cartProductVo);
                orderItem.setUserId(uid);
                orderItem.setOrderNo(orderNo);

                //创建orderItemVo，createTime还是空，需要自己设置
                OrderItemVo orderItemVo = new OrderItemVo();
                BeanUtils.copyProperties(orderItem, orderItemVo);
                orderItemVo.setCreateTime(new Date());

                //加入orderItemList和orderItemVoList
                orderItemList.add(orderItem);
                orderItemVoList.add(orderItemVo);
            }
        }

        /*
            3.生成订单，入库：order和order_item，事务
         */
        Order order = buildOrder(orderNo, uid, shippingId, cartVo, postage);

        //order写入数据库
        int rowOrder = orderMapper.insertSelective(order);
        if (rowOrder <= 0) {
            return ResponseVo.error(ResponseEnum.ORDER_CREATE_FAIL);
        }

        //orderItemList写入数据库
        int rowOrderItem = orderItemMapper.batchInsert(orderItemList);
        if (rowOrderItem <= 0) {
            return ResponseVo.error(ResponseEnum.ORDER_CREATE_FAIL);
        }

        /*
            4.减库存
         */
        //获取购物车中被选中的商品的idSet
        Set<Integer> productIdSet = cartProductVoList.stream()
                .filter(cartProductVo -> cartProductVo.isProductSelected())
                .map(cartProductVo -> cartProductVo.getProductId())
                .collect(Collectors.toSet());

        //通过idSet获取productList，这里又查了一次数据库，效率降低了，可以优化
        List<Product> productList = productMapper.selectByIdSet(productIdSet);

        //将productList转为productMap，提升查询效率
        Map<Integer, Product> productMap = productList.stream()
                .collect(Collectors.toMap(product -> product.getId(), product -> product));

        //遍历购物车中每一件商品的详情
        for (CartProductVo cartProductVo : cartProductVoList) {
            //必须选中
            if (cartProductVo.isProductSelected()) {
                //之前已经判断过商品是否下架或库存不够了，因此此处不再判断
                Product product = productMap.get(cartProductVo.getProductId());

                //减少选中商品的库存
                product.setStock(product.getStock() - cartProductVo.getQuantity());

                //写入数据库
                int count = productMapper.updateByPrimaryKeySelective(product);
                if (count <= 0) {
                    return ResponseVo.error(ResponseEnum.ORDER_CREATE_FAIL);
                }
            }
        }

        /*
            5.更新购物车
         */
        //根据uid获取redisKey
        String redisKey = String.format(MallConst.CART_REDIS_KEY_TEMPLATE, String.valueOf(uid));

        //拿到redisHash模板
        HashOperations<String, String, String>/*(redisKey,productId,json)*/
                opsForHash = stringRedisTemplate.opsForHash();

        for (CartProductVo cartProductVo : cartProductVoList) {
            //只删除选中的商品
            if (cartProductVo.isProductSelected()) {
                Long delete = opsForHash.delete(redisKey, String.valueOf(cartProductVo.getProductId()));
                if (delete <= 0) {
                    return ResponseVo.error(ResponseEnum.ORDER_CREATE_FAIL);
                }
            }
        }

        /*
            6.构造OrderVo
         */
        OrderVo orderVo = buildOrderVo(order, orderItemVoList, shippingVo);

        return ResponseVo.success(orderVo);
    }

    @Override
    public ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize) {
        //需要返回的orderVoList
        List<OrderVo> orderVoList = new ArrayList<>();

        //根据uid获取该用户所有订单
        List<Order> orderList = orderMapper.selectByUserId(uid);

        //获取uid的所有shippingId，并一次查出所有Shipping，放在Map中
        Set<Integer> shippingIdSet = orderList.stream()
                .map(Order::getShippingId)
                .collect(Collectors.toSet());
        List<Shipping> shippingList = shippingMapper.selectByIdSet(shippingIdSet);
        Map<Integer, Shipping> shippingMap = shippingList.stream()
                .collect(Collectors.toMap(Shipping::getId, shipping -> shipping));

        //同理，根据OrderNo一次查出所有OrderItem
        Set<Long> orderNoSet = orderList.stream()
                .map(Order::getOrderNo)
                .collect(Collectors.toSet());
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoSet(orderNoSet);

        //可以使用Collections.groupingBy将orderItemList中orderNo相同的元素打包变为Map
        Map<Long, List<OrderItem>> orderItemGroupMap =
                orderItemList.stream()
                        .collect(Collectors.groupingBy(OrderItem::getOrderNo));

        //遍历该用户所有订单
        for (Order order : orderList) {
            //获取order的shippingVo
            Shipping shipping = shippingMap.get(order.getShippingId());
            ShippingVo shippingVo = new ShippingVo();
            BeanUtils.copyProperties(shipping, shippingVo);

            //通过orderItemGroupMap获取List<OrderItem>，并转化为orderItemVoList
            List<OrderItemVo> orderItemVoList = orderItemGroupMap.get(order.getOrderNo()).stream()
                    .map(orderItem -> {
                        OrderItemVo orderItemVo = new OrderItemVo();
                        BeanUtils.copyProperties(orderItem, orderItemVo);
                        return orderItemVo;
                    }).collect(Collectors.toList());

            OrderVo orderVo = buildOrderVo(order, orderItemVoList, shippingVo);
            orderVoList.add(orderVo);
        }

        //启动分页
        PageHelper.startPage(pageNum, pageSize);

        //返回分页详情
        PageInfo pageInfo = new PageInfo(orderVoList);
        pageInfo.setList(orderVoList);
        return ResponseVo.success(pageInfo);
    }

    @Override
    public ResponseVo<OrderVo> detail(Integer uid, Long orderNo) {
        Order order = orderMapper.selectByUserIdAndOrderNo(uid, orderNo);

        //没有找到订单
        if (order == null) {
            return ResponseVo.error(ResponseEnum.ORDER_NOT_FOUND);
        }

        //获取order的shippingVo
        Shipping shipping = shippingMapper.selectByIdAndUserId(order.getShippingId(), uid);
        ShippingVo shippingVo = new ShippingVo();
        BeanUtils.copyProperties(shipping, shippingVo);

        //根据uid和orderNo获取orderItemList，stream+lambda得到orderItemVoList
        List<OrderItemVo> orderItemVoList = orderItemMapper.selectByUserIdAndOrderNo(uid, orderNo).stream()
                .map(orderItem -> {
                    OrderItemVo orderItemVo = new OrderItemVo();
                    BeanUtils.copyProperties(orderItem, orderItemVo);
                    return orderItemVo;
                }).collect(Collectors.toList());

        //获取orderVo
        OrderVo orderVo = buildOrderVo(order, orderItemVoList, shippingVo);

        //反射获取枚举类的Enum数组
        List<PaymentTypeEnum> paymentTypeEnums =
                Arrays.stream(PaymentTypeEnum.class.getEnumConstants())
                        .collect(Collectors.toList());

        //找到Enum数组中type和orderVo的paymentType一致的枚举
        List<PaymentTypeEnum> paymentType = paymentTypeEnums.stream()
                .filter(paymentTypeEnum -> paymentTypeEnum.getType().equals(orderVo.getPaymentType()))
                .collect(Collectors.toList());
        orderVo.setPaymentTypeDesc(paymentType.get(0).getDesc());

        //同理得到orderStatus
        List<OrderStatusEnum> orderStatus = Arrays.stream(OrderStatusEnum.class.getEnumConstants())
                .collect(Collectors.toList()).stream()
                .filter(e -> e.getStatus().equals(orderVo.getStatus()))
                .collect(Collectors.toList());
        orderVo.setStatusDesc(orderStatus.get(0).getDesc());

        return ResponseVo.success(orderVo);
    }

    @Override
    @Transactional//开启事务，本方法中所有DML处在同一个事务下
    public ResponseVo cancel(Integer uid, Long orderNo) {
        Order order = orderMapper.selectByUserIdAndOrderNo(uid, orderNo);

        //没有找到订单
        if (order == null) {
            return ResponseVo.error(ResponseEnum.ORDER_NOT_FOUND);
        }

        //订单已支付，无法取消
        if (order.getStatus().equals(OrderStatusEnum.PAYED.getStatus())) {
            return ResponseVo.error(ResponseEnum.ORDER_IS_PAYED);
        }

        /*
            1.将order的状态改为已取消，orderItem不变
         */
        order.setStatus(OrderStatusEnum.CANCELED.getStatus());
        int updateOrder = orderMapper.updateByPrimaryKeySelective(order);
        if (updateOrder <= 0) {
            return ResponseVo.error(ResponseEnum.ORDER_CANCEL_FAIL);
        }

        /*
            2.还原商品库存
         */
        //获取orderItemList，根据orderItemList获取订单中商品的idSet
        List<OrderItem> orderItemList = orderItemMapper.selectByUserIdAndOrderNo(uid, orderNo);

        Set<Integer> idSet = orderItemList.stream()
                .map(OrderItem::getProductId)
                .collect(Collectors.toSet());

        //通过idSet获取productList，并转成productMap提高查询效率
        List<Product> productList = productMapper.selectByIdSet(idSet);
        Map<Integer, Product> productMap = productList.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        //遍历orderItemList
        for (OrderItem orderItem : orderItemList) {
            Product product = productMap.get(orderItem.getProductId());

            //还原库存
            product.setStock(product.getStock() + orderItem.getQuantity());

            //更新数据库
            int count = productMapper.updateByPrimaryKeySelective(product);
            if (count <= 0) {
                return ResponseVo.error(ResponseEnum.ORDER_CANCEL_FAIL);
            }
        }

        /*
            3.还原购物车
         */
        //根据uid获取redisKey
        String redisKey = String.format(MallConst.CART_REDIS_KEY_TEMPLATE, String.valueOf(uid));

        //拿到redisHash模板
        HashOperations<String, String, String>/*(redisKey,productId,json)*/
                opsForHash = stringRedisTemplate.opsForHash();

        //遍历orderItemList，还原购物车，默认全部不选中
        for (OrderItem orderItem : orderItemList) {
            Cart cart = orderItemToCart(orderItem);

            opsForHash.put(redisKey,
                    String.valueOf(orderItem.getProductId()),
                    gson.toJson(cart));
        }

        return ResponseVo.success();
    }

    @Override
    public void paid(Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);

        if (order == null) {
            throw new RuntimeException(ResponseEnum.ORDER_NOT_FOUND.getMsg() + " " + orderNo.toString());
        }

        if (!order.getStatus().equals(OrderStatusEnum.NOT_PAY.getStatus())) {
            throw new RuntimeException(ResponseEnum.ORDER_STATUS_ERROR.getMsg() + " " + orderNo.toString());
        }

        order.setStatus(OrderStatusEnum.PAYED.getStatus());
        order.setPaymentTime(new Date());//支付时间
        int count = orderMapper.updateByPrimaryKeySelective(order);
        if (count <= 0) {
            throw new RuntimeException(ResponseEnum.ORDER_PAID_FAIL.getMsg() + " " + orderNo.toString());
        }
    }

    /**
     * 将CartProductVo对象中的部分属性附到OrderItem中
     *
     * @return OrderItem
     */
    private OrderItem cartProductVoToOrderItem(CartProductVo cartProductVo) {
        OrderItem orderItem = new OrderItem(
                cartProductVo.getProductId(),
                cartProductVo.getProductName(),
                cartProductVo.getProductMainImage(),
                cartProductVo.getProductPrice(),
                cartProductVo.getQuantity(),
                cartProductVo.getProductTotalPrice()
        );
        return orderItem;
    }

    /**
     * 通过OrderItem构造Cart对象
     *
     * @return Cart
     */
    private Cart orderItemToCart(OrderItem orderItem) {
        Cart cart = new Cart();//selected默认为false
        cart.setProductId(orderItem.getProductId());
        cart.setQuantity(orderItem.getQuantity());
        return cart;
    }

    @SuppressWarnings("all")
    /**
     * 生成Order对象
     *
     * @param orderNo
     * @param uid
     * @param shippingId
     * @param cartVo
     * @param postage
     * @return
     */
    private Order buildOrder(Long orderNo, Integer uid, Integer shippingId, CartVo cartVo, Integer postage) {
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(uid);
        order.setShippingId(shippingId);
        order.setPayment(cartVo.getCartTotalPrice());
        order.setPaymentType(PaymentTypeEnum.ONLINE_PAYMENT.getType());
        order.setPostage(postage);
        order.setStatus(OrderStatusEnum.NOT_PAY.getStatus());
        return order;
    }

    @SuppressWarnings("all")
    /**
     * 生成OrderVo对象
     *
     * @param order
     * @param orderItemVoList
     * @param shippingVo
     * @return
     */
    private OrderVo buildOrderVo(Order order, List<OrderItemVo> orderItemVoList, ShippingVo shippingVo) {
        OrderVo orderVo = new OrderVo();
        BeanUtils.copyProperties(order, orderVo);
        orderVo.setCreateTime(new Date());
        orderVo.setOrderItemVoList(orderItemVoList);
        orderVo.setReceiverName(shippingVo.getReceiverName());
        orderVo.setShippingVo(shippingVo);
        return orderVo;
    }

    /**
     * 生成orderNo（时间戳 + 三位随机数）
     * 企业级方法：分布式唯一id
     */
    private Long generateOrderNo() {
        //时间戳 + 三位随机数
        return System.currentTimeMillis() + new Random().nextInt(999);
    }
}
