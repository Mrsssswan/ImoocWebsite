package mrsssswan.mall.controller.portal;

import mrsssswan.mall.commons.Const;
import mrsssswan.mall.commons.ResponseCode;
import mrsssswan.mall.commons.ServerResponse;
import mrsssswan.mall.pojo.User;
import mrsssswan.mall.service.ICartService;
import mrsssswan.mall.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ICartService iCartService;

    /**
     * 列出所有购物车中的商品
     * @param session
     * @return
     */
    @ResponseBody
    @GetMapping("/list.do")
    public ServerResponse<CartVO> listProduct(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getMsg());
        }
        return iCartService.list(user.getId());
    }

    /**
     * 购物车添加商品
     * @param session
     * @param productId 商品ID
     * @param count 商品数量
     * @return
     */
    @ResponseBody
    @GetMapping("/add.do")
    public ServerResponse<CartVO> addProduct(HttpSession session, Integer productId, Integer count){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getMsg());
        }
        return iCartService.add(user.getId(),productId,count);
    }
    /**
     * 购物车更新商品
     * @param session
     * @param productId 商品ID
     * @param count 商品数量
     * @return
     */
    @ResponseBody
    @GetMapping("/update.do")
    public ServerResponse<CartVO> updateProduct(HttpSession session,Integer productId,Integer count){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getMsg());
        }
        return iCartService.update(user.getId(),productId,count);
    }
    /**
     * 购物车删除商品
     * @param session
     * @param productIds 因为可能同时删除多个商品，因此将商品ID看成一个字符串
     * @return
     */
    @ResponseBody
    @GetMapping("/delete.do")
    public ServerResponse<CartVO> deleteProduct(HttpSession session,String productIds){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getMsg());
        }
        return iCartService.delete(user.getId(),productIds);
    }

    /**
     * 全部勾选所有商品
     * @param session
     * @return
     */
    @ResponseBody
    @GetMapping("/check_all.do")
    public ServerResponse<CartVO> checkAllProduct(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getMsg());
        }
        return iCartService.checkOrUncheck(user.getId(),Const.Cart.CKECKED,null);
    }
    /**
     * 全部不勾选所有商品
     * @param session
     * @return
     */
    @ResponseBody
    @GetMapping("/uncheck_all.do")
    public ServerResponse<CartVO> uncheckAllProduct(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getMsg());
        }
        return iCartService.checkOrUncheck(user.getId(),Const.Cart.UNCKECKED,null);
    }

    /**
     * 单选某个商品
     * @param session
     * @param productId  商品ID
     * @return
     */
    @ResponseBody
    @GetMapping("/check.do")
    public ServerResponse<CartVO> checkProduct(HttpSession session,Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getMsg());
        }
        return iCartService.checkOrUncheck(user.getId(),Const.Cart.CKECKED,productId);
    }
    /**
     * 不勾选某个商品
     * @param session
     * @param productId  商品ID
     * @return
     */
    @ResponseBody
    @GetMapping("/uncheck.do")
    public ServerResponse<CartVO> uncheckProduct(HttpSession session,Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getMsg());
        }
        return iCartService.checkOrUncheck(user.getId(),Const.Cart.UNCKECKED,productId);
    }

    /**
     * 获取购物车中商品数量
     * @param session
     * @param productId
     * @return
     */
    @ResponseBody
    @GetMapping("/get_cart_product_count.do")
    public ServerResponse<Integer> getCartProductCount(HttpSession session,Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createBySuccess(0);
        }
        return iCartService.getProductCount(user.getId());
    }
}
