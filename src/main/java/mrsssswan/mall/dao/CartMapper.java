package mrsssswan.mall.dao;

import mrsssswan.mall.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectCartByUserIdProductId(@Param("userId")Integer userId,@Param("productId")Integer productId);

    List<Cart> selectCartByUserId(Integer userId);

    int selectCartProductCheckedStatusByUserId(Integer userId);

    int deleteByUserIdAndProductIds(@Param("userId")Integer userId,@Param("productIdList")List<String> productIdList);

    int updateCheckOrUncheckedProduct(@Param("userId")Integer userId,@Param("check")Integer check,@Param("productId")Integer productId);

    int selectProductCount(Integer userId);

     List<Cart> selectCheckedcartByUserId(Integer userid);
}