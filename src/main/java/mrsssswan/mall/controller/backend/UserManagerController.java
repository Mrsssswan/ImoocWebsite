package mrsssswan.mall.controller.backend;

import mrsssswan.mall.commons.Const;
import mrsssswan.mall.commons.ServerResponse;
import mrsssswan.mall.pojo.User;
import mrsssswan.mall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/user/")
public class UserManagerController {
    @Autowired
    private IUserService iUserService;

    @ResponseBody
    @PostMapping("login.do")
    public ServerResponse<User> login(String username, String password, HttpSession session){
        ServerResponse<User> response = iUserService.login(username,password);
        if(response.isSuccess()){
            if(response.getData().getRole()== Const.Role.ROLE_ADMIN){
               session.setAttribute(Const.CURRENT_USER,response.getData());
               return response;
            }else
                return ServerResponse.createByErrorMessage("不是管理员，无法登录");
        }
        return response;
    }

}
