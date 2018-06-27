package mrsssswan.mall.service;


import mrsssswan.mall.commons.ServerResponse;

import java.util.Map;


public interface IOrderService {
    ServerResponse pay(Long num, Integer id, String path);
    ServerResponse alipayCallback(Map<String,String> params);
    ServerResponse orderPayStatus(Integer id,Long orderNo);
}
