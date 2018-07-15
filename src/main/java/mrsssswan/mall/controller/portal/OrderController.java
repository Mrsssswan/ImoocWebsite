package mrsssswan.mall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import mrsssswan.mall.commons.Const;
import mrsssswan.mall.commons.ResponseCode;
import mrsssswan.mall.commons.ServerResponse;
import mrsssswan.mall.pojo.User;
import mrsssswan.mall.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


@RequestMapping("/order/")
@Controller
public class OrderController {
    private Logger log = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    private IOrderService iOrderService;

    /**
     * 创建订单
     * @param session
     * @param shippingId 购物车ID
     * @return
     */
    @ResponseBody
    @GetMapping("create.do")
    public ServerResponse pay(HttpSession session,Integer shippingId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getMsg());
        }
        return iOrderService.createOrder(user.getId(),shippingId);
    }

    /**
     * 取消订单
     * @param session
     * @param orderNo 订单号
     * @return
     */
    @ResponseBody
    @GetMapping("calcel.do")
    public ServerResponse calcel(HttpSession session,long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getMsg());
        }
        return iOrderService.cancelOrder(user.getId(),orderNo);
    }

    /**
     * 获取购物车中选中的商品
     * @param session
     */
    @ResponseBody
    @GetMapping("get_order_cart_product.do")
    public ServerResponse getOrderCartProduct(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getMsg());
        }
        return iOrderService.orderCartProduct(user.getId());
    }
    @ResponseBody
    @GetMapping("aplipay_callback.do")
    public Object aplipay_callback(HttpServletRequest request) {
        HashMap map = Maps.newHashMap();
        Map resultMap = request.getParameterMap();
        for (Iterator iterator = resultMap.entrySet().iterator(); iterator.hasNext(); ) {
            String name = (String) iterator.next();
            String[] values = (String[]) resultMap.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            map.put(name, valueStr);
        }
        log.info("支付宝回调,sign:{},trade_status:{},参数:{}", map.get("sign"), map.get("trade_status"), map.toString());
        //验证回调的正确性
        map.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(map, Configs.getAlipayPublicKey(), "urf8", Configs.getSignType());
            if (!alipayRSACheckedV2) {
                return ServerResponse.createByErrorMessage("非法请求");
            }
        } catch (AlipayApiException e) {
            log.info("支付宝回调异常", e);
        }
       //todo 验证各种数据
        ServerResponse serverResponse = iOrderService.alipayCallback(map);
        if(serverResponse.isSuccess()){
            return Const.AlipayCallback.RESPONSE_SUCCESS;
        }
        else
            return Const.AlipayCallback.RESPONSE_FAILED;
    }

    /**
     * 查询订单状态
     * @param session
     * @param num  订单号
     * @return 支付或未支付
     */
    @ResponseBody
    @GetMapping("query_order_pay_status.do")
    public ServerResponse<Boolean> queryOrderPayStatus(HttpSession session,  Long num) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getMsg());
        }
       ServerResponse response =  iOrderService.orderPayStatus(user.getId(),num);
        if(response.isSuccess()){
            return ServerResponse.createBySuccess(true);
        }else
            return ServerResponse.createBySuccess(false);
    }
}
