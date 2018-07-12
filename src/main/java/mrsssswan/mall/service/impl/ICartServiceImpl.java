package mrsssswan.mall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import mrsssswan.mall.commons.Const;
import mrsssswan.mall.commons.ResponseCode;
import mrsssswan.mall.commons.ServerResponse;
import mrsssswan.mall.dao.CartMapper;
import mrsssswan.mall.dao.ProductMapper;
import mrsssswan.mall.pojo.Cart;
import mrsssswan.mall.pojo.Product;
import mrsssswan.mall.service.ICartService;
import mrsssswan.mall.util.BigDecimalUtil;
import mrsssswan.mall.util.PropertiesUtil;
import mrsssswan.mall.vo.CartProductVO;
import mrsssswan.mall.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
@Service("iCartService")
public class ICartServiceImpl implements ICartService {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
    @Override
    public ServerResponse<CartVO> add(Integer userId, Integer productId, Integer count) {
        if(productId == null || count == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLGEALL_ARGUMENTS.getCode(),ResponseCode.ILLGEALL_ARGUMENTS.getMsg());
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId,productId);
        if(cart == null) {
            //这个产品不在这个购物车里，需要新增它的记录
            Cart cartItem = new Cart();
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartItem.setQuantity(count);
            cartItem.setChecked(Const.Cart.CKECKED);
            cartMapper.insert(cartItem);
        }else {
            //产品已存在，只需要数量相加
        count += cart.getQuantity();
        cart.setQuantity(count);
        cartMapper.updateByPrimaryKeySelective(cart);
        }
        return this.list(userId);

    }

    @Override
    public ServerResponse<CartVO> update(Integer userId, Integer productId, Integer count) {
        if(productId == null || count == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLGEALL_ARGUMENTS.getCode(),ResponseCode.ILLGEALL_ARGUMENTS.getMsg());
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId,productId);
        if(cart != null) {
            cart.setQuantity(count);
        }
        cartMapper.updateByPrimaryKeySelective(cart);
        return this.list(userId);
    }

    @Override
    public ServerResponse<CartVO> delete(Integer userId, String productIds) {
        List<String> productList = Splitter.on(",").splitToList(productIds);
        if(CollectionUtils.isEmpty(productList)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLGEALL_ARGUMENTS.getCode(),ResponseCode.ILLGEALL_ARGUMENTS.getMsg());
        }
        cartMapper.deleteByUserIdAndProductIds(userId,productList);
        return this.list(userId);
    }

    public  ServerResponse list (Integer userId){
        CartVO cartVO = getCartVolimit(userId);
        return ServerResponse.createBySuccess(cartVO);
    }

    @Override
    public ServerResponse<CartVO> checkOrUncheck(Integer userId, Integer check, Integer productId) {
        cartMapper.updateCheckOrUncheckedProduct(userId,check,productId);
        return this.list(userId);
    }

    @Override
    public ServerResponse<Integer> getProductCount(Integer userId) {
        if(userId == null){
            return ServerResponse.createBySuccess(0);
        }

        return ServerResponse.createBySuccess(cartMapper.selectProductCount(userId));
    }

    private CartVO getCartVolimit(Integer userId) {
        CartVO cartVO = new CartVO();
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        List<CartProductVO> cartProductVOList = Lists.newArrayList();
        BigDecimal totlePrice = new BigDecimal("0");
        if(!CollectionUtils.isEmpty(cartList)){
            for(Cart cartItem: cartList){
                CartProductVO cartProductVo = new CartProductVO();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setUserId(userId);
                cartProductVo.setProductId(cartItem.getProductId());

                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if(product!=null){
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());
                    //判断库存
                    int limit = 0;
                    //商品库存大于购物车加入的数量  库存充足
                    if(product.getStock() > cartItem.getQuantity()){
                        limit = cartItem.getQuantity();
                       cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    }else{
                        limit = product.getStock();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                        //购物车中更新有效库存
                        Cart cartForQuantity = new Cart();
                        cartForQuantity.setId(cartItem.getId());
                        cartForQuantity.setQuantity(limit);
                        cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                    }

                    cartProductVo.setQuantity(limit);
                    //计算总价
                    cartProductVo.setProductTotlePrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cartProductVo.getQuantity()));
                    cartProductVo.setProductChecked(cartItem.getChecked());

                }
                if(cartItem.getChecked() == Const.Cart.CKECKED){
                    //如果已经勾选,增加到整个的购物车总价中
                    totlePrice = BigDecimalUtil.add(totlePrice.doubleValue(),cartProductVo.getProductTotlePrice().doubleValue());
                }
                cartProductVOList.add(cartProductVo);
            }
        }
        cartVO.setTotlePrice(totlePrice);
        cartVO.setCartProductVOS(cartProductVOList);
        cartVO.setAllChecked(this.getAllCheckedStatus(userId));
        cartVO.setImageHost(PropertiesUtil.getProperty("sftp.server.prefix"));
        return cartVO;
    }

    private Boolean getAllCheckedStatus(Integer userId) {
        if(userId == null){
            return false;
        }
        return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0;
    }
}
