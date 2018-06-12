package mrsssswan.mall.controller;

import mrsssswan.mall.commons.Const;
import mrsssswan.mall.commons.ServerResponse;
import mrsssswan.mall.pojo.User;
import mrsssswan.mall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @ResponseBody
    @PostMapping("login.do")
    public ServerResponse<User> login(String username, String password, HttpSession session){
        ServerResponse<User> response = iUserService.login(username,password);
        if(response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }


    @ResponseBody
    @GetMapping("logout.do")
    public ServerResponse<String> logout( HttpSession session) {

        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }
    @ResponseBody
    @GetMapping("register.do")
    public ServerResponse<String> register(User user){
       return iUserService.register(user);
    }
    @ResponseBody
    @GetMapping("check_Valid.do")
    public ServerResponse<String> checkValid(String str,String type){
        return iUserService.checkValid(str,type);
    }

}
