package mrsssswan.mall.service;

import mrsssswan.mall.commons.ServerResponse;
import mrsssswan.mall.pojo.User;

public interface IUserService {

    ServerResponse<User> login(String username, String password);
    ServerResponse<String> register(User user);
    ServerResponse<String> checkValid(String str,String type);
    ServerResponse selectQuestion(String username);
    ServerResponse<String> checkAnswer(String username,String question,String answer);
    ServerResponse<String> forgetResetToken(String username,String passwordNew,String forgetToken);
    ServerResponse<String> resetpassword(String passwordOld, String passwordNew, User user);
    ServerResponse<User> updateImformation(User user);
    ServerResponse<User> getImformation(Integer id);
}
