package mrsssswan.mall.service;


import mrsssswan.mall.commons.ServerResponse;

import java.util.Map;


public interface IOrderService {
    ServerResponse createOrder(Integer userid,Integer shippingId);
    ServerResponse cancelOrder(Integer userid,long orderNo);
    ServerResponse orderCartProduct(Integer userid);
    ServerResponse orderList(Integer userid,int pageNum,int pageSize);
    ServerResponse manageOderList(int pageNum,int pageSize);
    ServerResponse manageOderDetail(long orderNo);
    ServerResponse manageOderSearch(long orderNo,int pageNum,int pageSize);
    ServerResponse manageOderSend(long orderNo);
    ServerResponse orderDetail(Integer userid,long orderNo);
    ServerResponse alipayCallback(Map<String,String> params);
    ServerResponse orderPayStatus(Integer id,Long orderNo);

}
