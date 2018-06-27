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

    int checkAnswer(@Param("username")String username,@Param("question")String question,@Param("answer")String answer);

    int updatePasswordByUsername(@Param("username") String username, @Param("passwordNew") String passwordNew);

    int checkPassword( @Param("passwordOld") String passwordOld, @Param("id") Integer id);

    int checkEmailByUserId( @Param("email") String email, @Param("id") Integer id);

    User login(@Param("username") String username, @Param("password") String password);
}