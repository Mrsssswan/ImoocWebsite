package mrsssswan.mall.service.impl;

import mrsssswan.mall.commons.ServerResponse;
import mrsssswan.mall.dao.UserMapper;
import mrsssswan.mall.pojo.User;
import mrsssswan.mall.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount =   userMapper.checkUsername(username);
        if(resultCount == 0){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        //todo md5密码加密
        User user = userMapper.login(username,password);
        if(user == null){
            return ServerResponse.createByErrorMessage("密码不正确");
        }
        //将密码设置为null
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功",user);
    }
}
