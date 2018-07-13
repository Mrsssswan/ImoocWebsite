package mrsssswan.mall.controller.portal;


import com.github.pagehelper.PageInfo;
import mrsssswan.mall.commons.Const;
import mrsssswan.mall.commons.ResponseCode;
import mrsssswan.mall.commons.ServerResponse;
import mrsssswan.mall.pojo.Shipping;
import mrsssswan.mall.pojo.User;
import mrsssswan.mall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@RequestMapping("shipping/")
@Controller
public class ShippingController {

    @Autowired
    private IShippingService iShippingService;

    /**
     * 新增地址
     * @param session
     * @param shipping 地址
     * @return
     */
    @ResponseBody
    @GetMapping("add_shipping.do")
    public ServerResponse addShipping(HttpSession session, Shipping shipping){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getMsg());
        }
        return iShippingService.add(user.getId(),shipping);

    }

    /**
     * 删除地址
     * @param session
     * @param shippingId 地址ID
     * @return
     */
    @ResponseBody
    @GetMapping("delete_shipping.do")
    public ServerResponse deleteShipping(HttpSession session, Integer shippingId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getMsg());
        }
        return iShippingService.delete(user.getId(),shippingId);

    }

    /**
     * 更新地址
     * @param session
     * @param shipping 新的地址
     * @return
     */
    @ResponseBody
    @GetMapping("update_shipping.do")
    public ServerResponse updateShipping(HttpSession session, Shipping shipping){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getMsg());
        }
        return iShippingService.update(user.getId(),shipping);

    }

    /**
     * 查询地址
     * @param session
     * @param shippingId 地址ID
     * @return
     */
    @ResponseBody
    @GetMapping("select_shipping.do")
    public ServerResponse selectShipping(HttpSession session, Integer shippingId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getMsg());
        }
        return iShippingService.select(user.getId(),shippingId);

    }

    /**
     * 查询所有地址
     * @param session
     * @param pageNum 分页数量
     * @param pageSize 分页大小
     * @return
     */
    @ResponseBody
    @GetMapping("list_shipping.do")
    public ServerResponse<PageInfo> listShipping(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "0") int pageNum,
                                                 @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getMsg());
        }
        return iShippingService.list(user.getId(),pageNum,pageSize);

    }
}
