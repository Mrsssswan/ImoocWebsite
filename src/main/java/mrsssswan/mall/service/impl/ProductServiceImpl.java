package mrsssswan.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import mrsssswan.mall.commons.ResponseCode;
import mrsssswan.mall.commons.ServerResponse;
import mrsssswan.mall.dao.CategoryMapper;
import mrsssswan.mall.dao.ProductMapper;
import mrsssswan.mall.pojo.Category;
import mrsssswan.mall.pojo.Product;
import mrsssswan.mall.service.IProductService;
import mrsssswan.mall.util.DateTimeUtil;
import mrsssswan.mall.util.PropertiesUtil;
import mrsssswan.mall.vo.ProductDetailVo;
import mrsssswan.mall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public ServerResponse saveOrUpdateProduct(Product product) {
        if(product != null){
            //将主图赋值
            if(StringUtils.isNotBlank(product.getSubImages())){
                String images[] = product.getSubImages().split(",");
                product.setMainImage(images[0]);
            }
            //更新产品时，id不为空
            if(product.getId() != null){
                int resultCount = productMapper.updateByPrimaryKeySelective(product);
                if(resultCount > 0){
                    return  ServerResponse.createBySuccessMessage("更新产品成功");
                }
                return ServerResponse.createByErrorMessage("更新产品失败");
            }//id为空说明是新增产品
            else{
                int resultCount = productMapper.insert(product);
                if(resultCount > 0){
                    return  ServerResponse.createBySuccessMessage("新增产品成功");
                }
                return ServerResponse.createByErrorMessage("新增产品失败");
            }

        }
        return ServerResponse.createByErrorMessage("参数错误");
    }

    @Override
    public ServerResponse setProductStatus(Integer productId, Integer status) {
        if(productId == null || status ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLGEALL_ARGUMENTS.getCode(),
                    ResponseCode.ILLGEALL_ARGUMENTS.getMsg());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(productId);
        int resultCount = productMapper.updateByPrimaryKeySelective(product);
        if(resultCount > 0){
            return  ServerResponse.createBySuccessMessage("更新产品销售状态成功");
        }
        return ServerResponse.createByErrorMessage("更新产品销售状态失败");
    }

    @Override
    public ServerResponse<Object> getProductDetail(Integer id) {
        if(id==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLGEALL_ARGUMENTS.getCode(),
                    ResponseCode.ILLGEALL_ARGUMENTS.getMsg());
        }
        Product product = productMapper.selectByPrimaryKey(id);
        if(product == null){
            return ServerResponse.createByErrorMessage("产品已下架或者删除");
        }
        ProductDetailVo productDetailVo = assemblyProductDetailVo(product);

        return ServerResponse.createBySuccess(productDetailVo);
    }

    /**
     * 获取商品详情
     * @param product
     * @return
     */
    private ProductDetailVo assemblyProductDetailVo(Product product){
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setId(product.getId());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setName(product.getName());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setImageHost(PropertiesUtil.getProperty("sftp.server.prefix","https://www.mrsssswan.club/"));
        Category category = categoryMapper.selectByPrimaryKey(product.getId());
        if(category == null){
            productDetailVo.setParentCategoryId(0);
        }else{
            productDetailVo.setParentCategoryId(category.getParentId());
        }
        productDetailVo.setCreateTime(DateTimeUtil.datarToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.datarToStr(product.getUpdateTime()));

        return productDetailVo;
    }


    @Override
    public ServerResponse<Object> getProductList(Integer pageNum, Integer pageSize) {
        //1.start page
        PageHelper.startPage(pageNum, pageSize);
        //2.list
        List<Product> products = productMapper.getList();
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for(Product product: products){
            ProductListVo productListVo = assemblyProductListVo(product);
            productListVoList.add(productListVo);
        }
        //3.收尾
        PageInfo pageresult = new PageInfo(products);
        pageresult.setList(productListVoList);
        return ServerResponse.createBySuccess(pageresult);
    }

    private ProductListVo assemblyProductListVo(Product product){
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setName(product.getName());
        productListVo.setPrice(product.getPrice());
        productListVo.setStatus(product.getStatus());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setImageHost(PropertiesUtil.getProperty("sftp.server.prefix","https://www.mrsssswan.club/"));
        return productListVo;
    }

    @Override
    public ServerResponse<Object> searchProduct(String name, Integer id, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        if(StringUtils.isNotBlank(name)){
            name = new StringBuilder().append("%").append(name).append("%").toString();
        }
        List<Product> products = productMapper.getListByNameAndId(name,id);
        List<ProductListVo> productListVos = Lists.newArrayList();
        for(Product product: products){
            ProductListVo productListVo = assemblyProductListVo(product);
            productListVos.add(productListVo);
        }
        PageInfo pageResult = new PageInfo(products);
        pageResult.setList(productListVos);
        return ServerResponse.createBySuccess(pageResult);
    }
}
