package mrsssswan.mall.controller.backend;

import com.github.pagehelper.util.StringUtil;
import com.google.common.collect.Maps;
import mrsssswan.mall.commons.Const;
import mrsssswan.mall.commons.ServerResponse;
import mrsssswan.mall.pojo.Product;
import mrsssswan.mall.pojo.User;
import mrsssswan.mall.service.IFileService;
import mrsssswan.mall.service.IUserService;
import mrsssswan.mall.service.impl.ProductServiceImpl;
import mrsssswan.mall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/manage/product")
public class ProductManageController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private ProductServiceImpl productService;
    @Autowired
    private IFileService iFileService;
    /**
     * 保存或者更新产品
     * @param session
     * @param product
     * @return
     */
    @ResponseBody
    @PostMapping("product_save.do")
    public ServerResponse productSave(HttpSession session, Product product){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorMessage("请登录");
        }

        if(iUserService.checkAdminRole(user).isSuccess()){
           return  productService.saveOrUpdateProduct(product);
        }
        return ServerResponse.createByErrorMessage("需要管理员权限");


    }

    /**
     * 更新产品状态 在售或者下架或者删除
     * @param session
     * @param id  产品id
     * @param status 产品状态
     * @return
     */
    @ResponseBody
    @GetMapping("set_product_status.do")
    public ServerResponse setProductStatus(HttpSession session, Integer id,Integer status){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorMessage("请登录");
        }

        if(iUserService.checkAdminRole(user).isSuccess()){
           return productService.setProductStatus(id,status);
        }
        return ServerResponse.createByErrorMessage("需要管理员权限");
    }

    /**
     * 获取产品详情
     * @param session
     * @param id
     * @return
     */
    @ResponseBody
    @GetMapping("product_detail.do")
    public ServerResponse getProductDetail(HttpSession session, Integer id){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorMessage("请登录");
        }

        if(iUserService.checkAdminRole(user).isSuccess()){
            return productService.getProductDetail(id);
        }
        return ServerResponse.createByErrorMessage("需要管理员权限");
    }

    /**
     * 列出产品
     * @param session
     * @param pageNum 分页数
     * @param pageSize 分页大小
     * @return
     */
    @ResponseBody
    @GetMapping("product_list.do")
    public ServerResponse getProductList(HttpSession session, @RequestParam(value = "pageNum" ,defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize" ,defaultValue = "10") Integer pageSize){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorMessage("请登录");
        }

        if(iUserService.checkAdminRole(user).isSuccess()){
            return productService.getProductList(pageNum,pageSize);
        }
        return ServerResponse.createByErrorMessage("需要管理员权限");
    }

    /**
     * 搜索产品
     * @param session
     * @param name 产品名
     * @param id 产品编号
     * @param pageNum 分页数
     * @param pageSize 分页大小
     * @return
     */
    @ResponseBody
    @GetMapping("product_search.do")
    public ServerResponse searchProduct(HttpSession session, String name,Integer id,
                                        @RequestParam(value = "pageNum" ,defaultValue = "1") Integer pageNum,
                                        @RequestParam(value = "pageSize" ,defaultValue = "10") Integer pageSize){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorMessage("请登录");
        }

        if(iUserService.checkAdminRole(user).isSuccess()){
            return productService.searchProduct(name,id,pageNum,pageSize);
        }
        return ServerResponse.createByErrorMessage("需要管理员权限");
    }
    @ResponseBody
    @PostMapping("img_upload.do")
    public Map img_upload(HttpSession session, MultipartFile file, HttpServletRequest request, HttpServletResponse response){
        Map resultMap = Maps.newHashMap();
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            resultMap.put("success",false);
            resultMap.put("msg","请登录管理员 ");
            return resultMap;
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file,path);
            if(StringUtils.isBlank(targetFileName)){
                resultMap.put("success",false);
                resultMap.put("msg","上传失败");
                return resultMap;
            }
            String url = PropertiesUtil.getProperty("sftp.server.prefix")+targetFileName;
            resultMap.put("success",true);
            resultMap.put("uri",targetFileName);
            resultMap.put("msg","上传成功");
            response.setHeader("Access-Control-Allow-Headers","X-File-Name");
            return resultMap;
        }
        resultMap.put("success",false);
        resultMap.put("msg","无权限操作");
        return resultMap;

    }
}
