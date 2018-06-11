package mrsssswan.mall.service;

import mrsssswan.mall.commons.ServerResponse;
import mrsssswan.mall.pojo.User;

public interface IUserService {

    ServerResponse<User> login(String username, String password);
}
