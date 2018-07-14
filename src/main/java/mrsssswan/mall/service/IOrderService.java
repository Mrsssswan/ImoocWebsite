package mrsssswan.mall.service;


import mrsssswan.mall.commons.ServerResponse;

import java.util.Map;


public interface IOrderService {
    ServerResponse createOrder(Integer userid,Integer shippingId);
    ServerResponse cancelOrder(Integer userid,long orderNo);
    ServerResponse orderCartProduct(Integer userid);
    ServerResponse alipayCallback(Map<String,String> params);
    ServerResponse orderPayStatus(Integer id,Long orderNo);

}
