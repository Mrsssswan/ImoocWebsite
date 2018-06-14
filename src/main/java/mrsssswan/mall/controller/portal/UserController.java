package mrsssswan.mall.controller.portal;

import mrsssswan.mall.commons.Const;
import mrsssswan.mall.commons.ResponseCode;
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
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @param session 存储的session
     * @return 返回响应结果
     */
    @ResponseBody
    @PostMapping("login.do")
    public ServerResponse<User> login(String username, String password, HttpSession session){
        ServerResponse<User> response = iUserService.login(username,password);
        if(response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    /**
     * 用户登出
     * @param session
     * @return
     */
    @ResponseBody
    @PostMapping("logout.do")
    public ServerResponse<String> logout( HttpSession session) {

        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @ResponseBody
    @PostMapping("register.do")
    public ServerResponse<String> register(User user){
       return iUserService.register(user);
    }

    /**
     *  根据type判断str
     * @param str 传入的用户名或邮箱
     * @param type 用户名或邮箱类型
     * @return
     */
    @ResponseBody
    @PostMapping("check_Valid.do")
    public ServerResponse<String> checkValid(String str,String type){
        return iUserService.checkValid(str,type);
    }

    /**
     * 获取用户登录信息
     * @param session
     * @return
     */
    @ResponseBody
    @PostMapping("get_user_info.do")
    public ServerResponse<User> getUserInfo(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user!=null)
            return ServerResponse.createBySuccess(user);
        return ServerResponse.createByErrorMessage("用户未登录");
    }

    /**
     * 忘记密码 找回密码问题
     * @param usename 用户名
     * @return 返回响应结果
     */
    @ResponseBody
    @PostMapping("forget_get_question.do")
    public ServerResponse forgetPassGetQuestion(String username){
        return iUserService.selectQuestion(username);
    }

    /**
     * 验证回答问题单是否正确
     * @param username 用户
     * @param question 问题
     * @param answer 答案
     * @return 返回响应结果
     */
    @ResponseBody
    @PostMapping("forget_check_answer.do")
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer){
        return iUserService.checkAnswer(username,question,answer);
    }

    /**
     * 用户回答忘记密码问题后重置密码
     * @param username 用户名
     * @param passwordNew 新密码
     * @param forgetToken 有效期
     * @return 返回响应结果
     */
    @ResponseBody
    @PostMapping("forget_reset_password.do")
    public ServerResponse<String> forgetResetpassword(String username, String passwordNew, String forgetToken){
        return iUserService.forgetResetToken(username,passwordNew,forgetToken);
    }

    @ResponseBody
    @PostMapping("reset_password.do")
    public ServerResponse<String> resetpassword(String passwordOld, String passwordNew, HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        return iUserService.resetpassword(passwordOld,passwordNew,user);
    }
    @ResponseBody
    @PostMapping("update_information.do")
    public ServerResponse<User> updateInformation(HttpSession session, User user){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response = iUserService.updateImformation(user);
        if(response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }
    @ResponseBody
    @PostMapping("get_information.do")
    public ServerResponse<User> getInformation(HttpSession session){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，需要登录");
        }

        return iUserService.getImformation(currentUser.getId());
    }
}
