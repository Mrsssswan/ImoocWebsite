package mrsssswan.mall.service;

import mrsssswan.mall.commons.ServerResponse;
import mrsssswan.mall.pojo.User;

public interface IUserService {

    ServerResponse<User> login(String username, String password);
    ServerResponse<String> register(User user);
    ServerResponse<String> checkValid(String str,String type);
    ServerResponse<String> selectQuestion(String username);
    ServerResponse<String> checkAnswer(String username,String question,String answer);
    ServerResponse<String> forgetResetToken(String username,String passwordNew,String forgetToken);
}
