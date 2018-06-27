package mrsssswan.mall.service;

import mrsssswan.mall.commons.ServerResponse;
import mrsssswan.mall.pojo.Product;

public interface IProductService {

     ServerResponse saveOrUpdateProduct(Product product);
     ServerResponse setProductStatus(Integer productId, Integer status);
     ServerResponse<Object>  getProductDetail(Integer id);
     ServerResponse<Object>  getProductList(Integer pageNum,Integer pageSize);
     ServerResponse<Object>  searchProduct(String name,Integer id,Integer pageNum,Integer pageSize);
}
