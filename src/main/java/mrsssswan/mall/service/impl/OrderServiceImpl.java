package mrsssswan.mall.service.impl;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jcraft.jsch.SftpException;
import mrsssswan.mall.commons.Const;
import mrsssswan.mall.commons.ServerResponse;
import mrsssswan.mall.dao.*;
import mrsssswan.mall.pojo.*;
import mrsssswan.mall.service.IOrderService;
import mrsssswan.mall.util.BigDecimalUtil;
import mrsssswan.mall.util.DateTimeUtil;
import mrsssswan.mall.util.PropertiesUtil;
import mrsssswan.mall.util.SFTPUtils;
import mrsssswan.mall.vo.OrderItemVo;
import mrsssswan.mall.vo.OrderProductVO;
import mrsssswan.mall.vo.OrderVO;
import mrsssswan.mall.vo.ShippingVo;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.*;

@Service("iOrderService")
public class OrderServiceImpl implements IOrderService{
    private static Log log = LogFactory.getLog(OrderServiceImpl.class);
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private PayInfoMapper payInfoMapper;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public ServerResponse createOrder(Integer userid,Integer shippingId){
        List<Cart> cartList = cartMapper.selectCheckedcartByUserId(userid);
        ServerResponse <List<OrderItem>> response = this.getCartOrderItem(userid,cartList);
        if(!response.isSuccess()){
            return response;
        }
        //计算订单总价
        List<OrderItem> orderItemList = response.getData();
        BigDecimal price = this.getTottlePrice(orderItemList);
        //生成订单
        Order order = assemblyOrder(userid,shippingId,price);
        if(order == null){
            return ServerResponse.createByErrorMessage("生成订单失败");
        }
        if(CollectionUtils.isEmpty(orderItemList)){
            return ServerResponse.createByErrorMessage("购物车是空的");
        }
        //mybatis批量插入
        orderItemMapper.batchInsert(orderItemList);
        //减少产品库存
        reduceProductStock(orderItemList);
        //清空购物车
        cleanCart(cartList);
        //返回给前端的数据
        OrderVO orderVO = assemblyOrderVO(order,orderItemList);
        return ServerResponse.createBySuccess(orderVO);
    }

    @Override
    public ServerResponse cancelOrder(Integer userid, long orderNo) {
        Order order = orderMapper.selectUserIdAndOrderId(userid,orderNo);
        if(order==null){
            return ServerResponse.createByErrorMessage("该用户订单不存在");
        }
        if(order.getStatus() == Const.OrderStatusEnum.PAID.getCode()){
            return ServerResponse.createByErrorMessage("已付款，无法取消");
        }
        Order updateOrder = new Order();
        updateOrder.setId(order.getId());
        updateOrder.setStatus(Const.OrderStatusEnum.CANCEL.getCode());
        int row = orderMapper.updateByPrimaryKeySelective(updateOrder);
        if(row>0){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

    @Override
    public ServerResponse orderCartProduct(Integer userid) {
        OrderProductVO orderProductVO = new OrderProductVO();
        //从购物车中取数据
        List<Cart> cartList = cartMapper.selectCartByUserId(userid);
        ServerResponse<List<OrderItem>> response = this.getCartOrderItem(userid,cartList);
        if(!response.isSuccess()){
            return response;
        }
        List<OrderItem> orderItemList = response.getData();
        List<OrderItemVo> orderItemVoList = Lists.newArrayList();
        BigDecimal price = new BigDecimal("0");
        for(OrderItem orderItem:orderItemList){
            price = BigDecimalUtil.add(price.doubleValue(),orderItem.getTotalPrice().doubleValue());
            orderItemVoList.add(assembleOrderItemVo(orderItem));
        }
        orderProductVO.setOrderItemList(orderItemVoList);
        orderProductVO.setTotlePrice(price);
        orderProductVO.setImageHost(PropertiesUtil.getProperty("sftp.server.prefix"));
        return ServerResponse.createBySuccess(orderProductVO);
    }

    @Override
    public ServerResponse<PageInfo> orderList(Integer userid,int pageNum,int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList = orderMapper.selectAllByUserId(userid);
        if(CollectionUtils.isEmpty(orderList)){
            return ServerResponse.createByErrorMessage("用户还没有任何订单");
        }
        List<OrderVO> orderVOList = assemblyOrderVOList(orderList,userid);
        PageInfo pageInfo = new PageInfo(orderList);
        pageInfo.setList(orderVOList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServerResponse<PageInfo> manageOderList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList = orderMapper.selectAllOrder();
        if(CollectionUtils.isEmpty(orderList)){
            return ServerResponse.createByErrorMessage("用户还没有任何订单");
        }
        List<OrderVO> orderVOList = assemblyOrderVOList(orderList,null);
        PageInfo pageInfo = new PageInfo(orderList);
        pageInfo.setList(orderVOList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServerResponse manageOderDetail(long orderNo) {
        Order order = orderMapper.selectByorderNo(orderNo);
        if(order != null){
            List<OrderItem> orderItemList = orderItemMapper.getByOrderNo(orderNo);
            OrderVO orderVO = assemblyOrderVO(order,orderItemList);
            return ServerResponse.createBySuccess(orderVO);
        }
        return ServerResponse.createByErrorMessage("订单不存在");
    }

    @Override
    public ServerResponse<PageInfo> manageOderSearch(long orderNo,int pageNum,int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        Order order = orderMapper.selectByorderNo(orderNo);
        if(order != null){
            List<OrderItem> orderItemList = orderItemMapper.getByOrderNo(orderNo);
            OrderVO orderVO = assemblyOrderVO(order,orderItemList);
            PageInfo pageInfo = new PageInfo(Arrays.asList(order));
            pageInfo.setList(Arrays.asList(orderVO));
            return ServerResponse.createBySuccess(pageInfo);
        }
        return ServerResponse.createByErrorMessage("订单不存在");
    }

    @Override
    public ServerResponse<String> manageOderSend(long orderNo) {
        Order order = orderMapper.selectByorderNo(orderNo);
        if(order!=null){
            if(order.getStatus() == Const.OrderStatusEnum.PAID.getCode()){
                order.setStatus(Const.OrderStatusEnum.SHIPPING.getCode());
                order.setSendTime(new Date());
                orderMapper.updateByPrimaryKeySelective(order);
                return ServerResponse.createBySuccess("发货成功");
            }
        }
        return ServerResponse.createByErrorMessage("订单不存在");
    }


    private List<OrderVO> assemblyOrderVOList(List<Order> orderList, Integer userid) {
        List<OrderVO> orderVOList = Lists.newArrayList();
        for(Order order:orderList){
            List<OrderItem> orderItemList = Lists.newArrayList();
            // 管理员查看用户订单
            if(userid == null) {
                orderItemList = orderItemMapper.getByOrderNo(order.getOrderNo());
            }
            else {
                orderItemList = orderItemMapper.getByOrderNoAndUserId(order.getOrderNo(),userid);
            }
            OrderVO orderVO = assemblyOrderVO(order,orderItemList);
            orderVOList.add(orderVO);
        }
         return orderVOList;
    }

    @Override
    public ServerResponse<OrderVO> orderDetail(Integer userid, long orderNo) {
        Order order = orderMapper.selectUserIdAndOrderId(userid,orderNo);
        if(order==null){
            return ServerResponse.createByErrorMessage("该用户订单不存在");
        }
        List<OrderItem> orderItemList = orderItemMapper.getByOrderNoAndUserId(orderNo,userid);
        OrderVO orderVO = assemblyOrderVO(order,orderItemList);
        return ServerResponse.createBySuccess(orderVO);
    }

    private OrderVO assemblyOrderVO(Order order, List<OrderItem> orderItemList) {
        OrderVO orderVo = new OrderVO();
        orderVo.setOrderNo(order.getOrderNo());
        orderVo.setPayment(order.getPayment());
        orderVo.setPaymentType(order.getPaymentType());
        orderVo.setPaymentTypeDesc(Const.PaymentTypeEnum.codeToMsg(order.getPaymentType()).getValue());

        orderVo.setPostage(order.getPostage());
        orderVo.setStatus(order.getStatus());
        orderVo.setStatusDesc(Const.OrderStatusEnum.codeToMsg(order.getStatus()).getValue());

        orderVo.setShippingId(order.getShippingId());
        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());
        if(shipping != null){
            orderVo.setReceiverName(shipping.getReceiverName());
            orderVo.setShippingVo(assembleShippingVo(shipping));
        }

        orderVo.setPaymentTime(DateTimeUtil.datarToStr(order.getPaymentTime()));
        orderVo.setSendTime(DateTimeUtil.datarToStr(order.getSendTime()));
        orderVo.setEndTime(DateTimeUtil.datarToStr(order.getEndTime()));
        orderVo.setCreateTime(DateTimeUtil.datarToStr(order.getCreateTime()));
        orderVo.setCloseTime(DateTimeUtil.datarToStr(order.getCloseTime()));


        orderVo.setImageHost(PropertiesUtil.getProperty("sftp.server.prefix"));


        List<OrderItemVo> orderItemVoList = Lists.newArrayList();

        for(OrderItem orderItem : orderItemList){
            OrderItemVo orderItemVo = assembleOrderItemVo(orderItem);
            orderItemVoList.add(orderItemVo);
        }
        orderVo.setOrderItemVoList(orderItemVoList);
        return orderVo;
    }

    private OrderItemVo assembleOrderItemVo(OrderItem orderItem) {
        OrderItemVo orderItemVo = new OrderItemVo();
        orderItemVo.setOrderNo(orderItem.getOrderNo());
        orderItemVo.setProductId(orderItem.getProductId());
        orderItemVo.setProductName(orderItem.getProductName());
        orderItemVo.setProductImage(orderItem.getProductImage());
        orderItemVo.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
        orderItemVo.setQuantity(orderItem.getQuantity());
        orderItemVo.setTotalPrice(orderItem.getTotalPrice());

        orderItemVo.setCreateTime(DateTimeUtil.datarToStr(orderItem.getCreateTime()));
        return orderItemVo;
    }

    private ShippingVo assembleShippingVo(Shipping shipping) {
        ShippingVo shippingVo = new ShippingVo();
        shippingVo.setReceiverName(shipping.getReceiverName());
        shippingVo.setReceiverAddress(shipping.getReceiverAddress());
        shippingVo.setReceiverProvince(shipping.getReceiverProvince());
        shippingVo.setReceiverCity(shipping.getReceiverCity());
        shippingVo.setReceiverDistrict(shipping.getReceiverDistrict());
        shippingVo.setReceiverMobile(shipping.getReceiverMobile());
        shippingVo.setReceiverZip(shipping.getReceiverZip());
        shippingVo.setReceiverPhone(shippingVo.getReceiverPhone());
        return shippingVo;
    }

    private void cleanCart(List<Cart> cartList) {
        for(Cart cart:cartList){
            cartMapper.deleteByPrimaryKey(cart.getId());
        }
    }

    private void reduceProductStock(List<OrderItem> orderItemList) {
        for(OrderItem orderItem:orderItemList){
            Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            product.setStock(product.getStock()-orderItem.getQuantity());
            productMapper.updateByPrimaryKeySelective(product);
        }
    }

    private Order assemblyOrder(Integer userid, Integer shippingId, BigDecimal price) {
        Order order = new Order();
        long orderNo = this.generatorOrderNo();
        order.setOrderNo(orderNo);
        order.setStatus(Const.OrderStatusEnum.NO_PAY.getCode());
        order.setPostage(0);
        order.setPaymentType(Const.PaymentTypeEnum.OnLINE_PAY.getCode());
        order.setUserId(userid);
        order.setShippingId(shippingId);
        order.setPayment(price);
        //发货时间
        //付款时间

        int row = orderMapper.insert(order);
            if(row>0){
                return order;
            }

        return null;
    }

    //生成订单号
    private long generatorOrderNo(){
        long currentTime = System.currentTimeMillis();
        return currentTime+ new Random().nextInt();
    }

    private BigDecimal getTottlePrice(List<OrderItem> orderItemList){
        BigDecimal payment = new BigDecimal("0");
        for(OrderItem orderItem:orderItemList){
           payment = BigDecimalUtil.add(payment.doubleValue(),orderItem.getTotalPrice().doubleValue());
        }
        return payment;
    }
    private ServerResponse<List<OrderItem>> getCartOrderItem(Integer userid, List<Cart> cartList){
        List<OrderItem> orderItemList = Lists.newArrayList();
        if(CollectionUtils.isEmpty(cartList)){
            return ServerResponse.createByErrorMessage("购物车为空");
        }
        for(Cart cart:cartList){
            OrderItem orderItem = new OrderItem();
            Product product = productMapper.selectByPrimaryKey(cart.getProductId());
            if(Const.ProductStatus.On_SALE.getCode() != product.getStatus()){
                return ServerResponse.createByErrorMessage("此商品暂不售卖");
            }
            if(cart.getQuantity()>product.getStock()){
                return ServerResponse.createByErrorMessage("产品"+product.getName()+"库存不足");
            }
            orderItem.setUserId(userid);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cart.getQuantity()));
            orderItemList.add(orderItem);
        }
        return ServerResponse.createBySuccess(orderItemList);
    }

    public ServerResponse pay(Long num, Integer id, String path) {
        //返回一个map 将订单号和生成的二维码返回给前端
        HashMap<String,String> map = Maps.newHashMap();
        Order order = orderMapper.selectUserIdAndOrderId(id,num);
        if (order == null){
            ServerResponse.createByErrorMessage("用户没有该订单");
        }
        map.put("orderNo",String.valueOf(order.getOrderNo()));
        // 测试当面付2.0生成支付二维码
        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = order.getOrderNo().toString();

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = new StringBuilder().append("快乐书城扫码支付").append(outTradeNo).toString();

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPayment().toString();

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = new StringBuilder().append("订单：").append(order.getOrderNo()).append("购买商品一共").append(totalAmount).append("元").toString();
        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        List<OrderItem> orderItems = orderItemMapper.getByOrderNoAndUserId(num,id);
        for(OrderItem item:orderItems){
            GoodsDetail goods1 = GoodsDetail.newInstance(item.getProductId().toString(), item.getProductName(),
                    BigDecimalUtil.mul(item.getCurrentUnitPrice().doubleValue(),new Double(100).doubleValue()).longValue(), item.getQuantity());
            goodsDetailList.add(goods1);
        }

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                .setNotifyUrl(PropertiesUtil.getProperty("alipay.callback.url"))//回调地址
                .setGoodsDetailList(goodsDetailList);
        // 支付宝当面付2.0服务
        Configs.init("zfbinfo.properties");
        AlipayTradeService tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);
                //生成的二维码存放的路径
                File folder = new File(path);
                if(!folder.exists()){
                    folder.setWritable(true);
                    folder.mkdirs();
                }

                // 需要修改为运行机器上的路径
                String qrPath = String.format(path+"/qr-%s.png", response.getOutTradeNo());
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrPath);
                try {
                    File targetFile = new File(qrPath);
                    SFTPUtils.uploadFile(path,targetFile);
                } catch (FileNotFoundException e) {
                   log.info("二维码失效");
                } catch (SftpException e) {
                    log.info("上传二维码异常");
                }
                // todo 获取上传到sftp服务器上的二维码地址
                String qrUrl = PropertiesUtil.getProperty("stpt.server.prefix")+ qrPath;
                map.put("qrUrl",qrUrl);
                return ServerResponse.createBySuccess(map);

            case FAILED:
                log.error("支付宝预下单失败!!!");
                return ServerResponse.createByErrorMessage("支付宝预下单失败!!!");

            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                return ServerResponse.createByErrorMessage("系统异常，预下单状态未知!!!");

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
               return ServerResponse.createByErrorMessage("不支持的交易状态，交易返回异常!!!");
        }
    }

    // 简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }
    @Override
    public ServerResponse alipayCallback(Map<String,String> params){
        Long orderNo = Long.parseLong(params.get("out_trade_no")); //商家订单号
        String tradeNo = params.get("trade_no"); //支付宝交易号
        String tradeStatus = params.get("trade_status"); //交易状态
        Order order = orderMapper.selectByorderNo(orderNo);
        if(order == null){
            ServerResponse.createByErrorMessage("非正常订单，回调忽略");
        }
        if(order.getStatus() >= Const.OrderStatusEnum.PAID.getCode()){
            ServerResponse.createBySuccessMessage("支付宝重复调用");
        }
        if(Const.AlipayCallback.TRADE_STATUS_TRADE_SUCCESS.equals(tradeStatus)){
            order.setPaymentTime(DateTimeUtil.strToDate(params.get("gmt_payment")));
            order.setStatus(Const.OrderStatusEnum.PAID.getCode());
            orderMapper.updateByPrimaryKeySelective(order);
        }
        //支付信息
        PayInfo payInfo = new PayInfo();
        payInfo.setUserId(order.getUserId());
        payInfo.setOrderNo(order.getOrderNo());
        payInfo.setPlatformStatus(tradeStatus);
        payInfo.setPlatformNumber(tradeNo);
        // todo 设置交易平台

        payInfoMapper.insert(payInfo);
        return ServerResponse.createBySuccess();
    }


    @Override
    public ServerResponse orderPayStatus(Integer id, Long orderNo) {
        Order order = orderMapper.selectUserIdAndOrderId(id,orderNo);
        if(order == null){
          return  ServerResponse.createByErrorMessage("用户没有该订单");
        }
        if(order.getStatus() >= Const.OrderStatusEnum.PAID.getCode()){
           return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }
}
