package mrsssswan.mall.dao;

import mrsssswan.mall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUsername(String username);

    int checkEmail(String email);

    String selectQuestion(String username);

    int checkAnswer(String username,String question,String answer);

    User login(@Param("username") String username, @Param("password") String password);
}