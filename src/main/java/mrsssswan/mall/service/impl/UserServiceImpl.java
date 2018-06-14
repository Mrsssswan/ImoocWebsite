package mrsssswan.mall.service.impl;

import mrsssswan.mall.commons.Const;
import mrsssswan.mall.commons.ServerResponse;
import mrsssswan.mall.commons.TokenCache;
import mrsssswan.mall.dao.UserMapper;
import mrsssswan.mall.pojo.User;
import mrsssswan.mall.service.IUserService;
import mrsssswan.mall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
        String md5Pass = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.login(username,md5Pass);
        if(user == null){
            return ServerResponse.createByErrorMessage("密码不正确");
        }
        //将密码设置为null
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功",user);
    }

    @Override
    public ServerResponse<String> register(User user) {
       ServerResponse<String> validResponse = this.checkValid(user.getUsername(),Const.USERNAME);
       if(!validResponse.isSuccess())
           return validResponse;
        validResponse = this.checkValid(user.getEmail(),Const.EMAIL);
        if(!validResponse.isSuccess())
            return validResponse;
        user.setRole(Const.Role.ROLE_CUSTOMER);//设置类型
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if(resultCount == 0){
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccess("注册成功");
    }


    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if(StringUtils.isNotBlank(type)){
            //校验用户名
            if(Const.USERNAME.equals(type)){
                int resultCount = userMapper.checkUsername(str);
                if(resultCount > 0 ){
                    return ServerResponse.createByErrorMessage("用户名已注册");
                }
            }
            if(Const.EMAIL.equals(type)){
                int resultCount =   userMapper.checkEmail(str);
                if(resultCount > 0){
                    return ServerResponse.createByErrorMessage("邮箱已注册");
                }
            }
        }else{
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }
    @Override
    public ServerResponse selectQuestion(String username) {
        ServerResponse validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess())
            return ServerResponse.createByErrorMessage("用户不存在");
        String question =  userMapper.selectQuestion(username);
        if(StringUtils.isNotBlank(question))
            return ServerResponse.createBySuccess(question);
        return ServerResponse.createByErrorMessage("找回密码的问题是空的");
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount =   userMapper.checkAnswer(username,question,answer);
        if(resultCount > 0){
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
         return ServerResponse.createByErrorMessage("问题答案错误");
    }

    @Override
    public ServerResponse<String> forgetResetToken(String username, String passwordNew, String forgetToken) {
        if(StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMessage("参数错误，需要传递token");
        }
        ServerResponse validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if(StringUtils.isBlank(token))
            return ServerResponse.createByErrorMessage("token无效或过期");
        if(StringUtils.equals(token,forgetToken)){
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int reslutCount = userMapper.updatePasswordByUsername(username,md5Password);
            if(reslutCount > 0)
                return ServerResponse.createBySuccessMessage("修改密码成功");
            else
                return ServerResponse.createByErrorMessage("token错误，请重新获取充值密码的token");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    @Override
    public ServerResponse<String> resetpassword(String passwordOld, String passwordNew, User user) {
        //防止横向越权，旧密码一定要指定用户
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
        if(resultCount==0){
            return ServerResponse.createByErrorMessage("旧密码错误");
        }
            user.setPassword(MD5Util.MD5EncodeUtf8(passwordOld));
            resultCount =  userMapper.updateByPrimaryKeySelective(user);
            if(resultCount > 0){
                return ServerResponse.createBySuccess("修改密码成功");
            }
           return ServerResponse.createByErrorMessage("修改密码失败");
    }

    @Override
    public ServerResponse<User> updateImformation(User user) {
        //需要校验email
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if(resultCount>0){
            return ServerResponse.createByErrorMessage("邮箱已被占用");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setUsername(user.getUsername());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        resultCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(resultCount==0){
            return ServerResponse.createByErrorMessage("用户个人信息更新失败");
        }
        return ServerResponse.createBySuccess("更新成功",updateUser);
    }

    @Override
    public ServerResponse<User> getImformation(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        if(user==null){
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }
}
