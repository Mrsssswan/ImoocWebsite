package mrsssswan.mall.controller.backend;

import com.github.pagehelper.PageInfo;
import mrsssswan.mall.commons.Const;
import mrsssswan.mall.commons.ResponseCode;
import mrsssswan.mall.commons.ServerResponse;
import mrsssswan.mall.pojo.User;
import mrsssswan.mall.service.IOrderService;
import mrsssswan.mall.service.IUserService;
import mrsssswan.mall.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manager/order/")
public class OrderManagerController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IOrderService iOrderService;

    /**
     * 列出所有订单
     *
     * @param session
     * @param pageNum  分页数量
     * @param pageSize 分页大小
     * @return
     */
    @ResponseBody
    @GetMapping("order_list.do")
    public ServerResponse<PageInfo> orderList(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getMsg());
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iOrderService.manageOderList(pageNum, pageSize);
        }
        return ServerResponse.createByErrorMessage("需要管理员权限");
    }

    /**
     * 查询某订单详情
     *
     * @param session
     * @param orderNo 订单号
     * @return
     */
    @ResponseBody
    @GetMapping("order_detail.do")
    public ServerResponse<OrderVO> orderList(HttpSession session, long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getMsg());
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iOrderService.manageOderDetail(orderNo);

        }
        return ServerResponse.createByErrorMessage("需要管理员权限");
    }

    /**
     * 搜素某订单，精确查找，包括该订单的地址详情等信息
     *
     * @param session
     * @param orderNo 订单号
     * @param pageNum  分页数量
     * @param pageSize 分页大小
     * @return
     */
    @ResponseBody
    @GetMapping("order_search.do")
    public ServerResponse<PageInfo> orderSearch(HttpSession session, long orderNo,@RequestParam(value = "pageNum",defaultValue = "0")Integer pageNum,
                                               @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getMsg());
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iOrderService.manageOderSearch(orderNo,pageNum,pageSize);

        }
        return ServerResponse.createByErrorMessage("需要管理员权限");
    }

    /**
     * 商家发货
     *
     * @param session
     * @param orderNo 订单号
     * @return
     */
    @ResponseBody
    @GetMapping("order_send.do")
    public ServerResponse<String> orderSend(HttpSession session, long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getMsg());
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iOrderService.manageOderSend(orderNo);

        }
        return ServerResponse.createByErrorMessage("需要管理员权限");
    }
}
