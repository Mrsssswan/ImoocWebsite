package mrsssswan.mall.service;

import mrsssswan.mall.commons.ServerResponse;
import mrsssswan.mall.vo.CartVO;

public interface ICartService {
   ServerResponse<CartVO> add(Integer userId, Integer productId, Integer count);
   ServerResponse<CartVO> update(Integer userId,Integer productId, Integer count);
   ServerResponse<CartVO> delete(Integer userId,String productIds);
   ServerResponse<CartVO> list(Integer userId);
   ServerResponse<CartVO> checkOrUncheck(Integer userId,Integer check,Integer productId);
   ServerResponse<Integer> getProductCount(Integer userId);
}
