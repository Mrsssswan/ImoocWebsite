package mrsssswan.mall.controller.backend;

import mrsssswan.mall.commons.Const;
import mrsssswan.mall.commons.ServerResponse;
import mrsssswan.mall.pojo.User;
import mrsssswan.mall.service.ICategaoryService;
import mrsssswan.mall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICategaoryService iCategaoryService;

    /**
     * 添加商品
     * @param categoryName 商品名
     * @param parentId 父节点 上一级分类节点
     * @param session
     * @return
     */
    @ResponseBody
    @PostMapping("add_category.do")
    public ServerResponse addCategory(String categoryName, @RequestParam(value = "parentId",defaultValue = "0") Integer parentId, HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorMessage("请登录");
        }
        //判断是否是管理员
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iCategaoryService.addCategory(categoryName,parentId);
        }
        return ServerResponse.createByErrorMessage("需要管理员权限");
    }

    /**
     * 更新商品名
     * @param categoryName
     * @param categoryId 商品编号
     * @param session
     * @return
     */
    @ResponseBody
    @PostMapping("update_category.do")
    public ServerResponse updateCategory(String categoryName,Integer categoryId,HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorMessage("请登录");
        }
        //判断是否是管理员
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iCategaoryService.updateCategoryName(categoryId,categoryName);
        }
        return ServerResponse.createByErrorMessage("需要管理员权限");
    }

    /**
     * 获取该分类的所有平行的子商品
     * @param categoryId 该分类id
     * @param session
     * @return
     */
    @ResponseBody
    @GetMapping("get_children_parallel_category.do")
    public ServerResponse getChildrenParallelCategory(@RequestParam(value = "categoryId",defaultValue = "0")Integer categoryId,HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorMessage("请登录");
        }
        //查询平行子节点
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iCategaoryService.getChildrenParallelCategory(categoryId);
        }
        return ServerResponse.createByErrorMessage("需要管理员权限");
    }

    /**
     * 获取平行以及递归所有子分类的子商品
     * @param categoryId
     * @param session
     * @return
     */
    @ResponseBody
    @GetMapping("get_category_deep_children.do")
    public ServerResponse getCategoryAndChildrenDeepCategory(@RequestParam(value = "categoryId",defaultValue = "0")Integer categoryId,HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorMessage("请登录");
        }
        //查询平行以及递归子节点
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iCategaoryService.getCategoryAndChildrenDeepCategory(categoryId);
        }
        return ServerResponse.createByErrorMessage("需要管理员权限");
    }
}
