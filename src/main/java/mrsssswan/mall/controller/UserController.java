package mrsssswan.mall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller("/user/")
public class UserController {

    @ResponseBody
    @PostMapping("login.do")
    public Object login(String username, String password, HttpSession session){


        return null;
    }

}
