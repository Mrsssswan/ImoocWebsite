package mrsssswan.mall.dao;

import mrsssswan.mall.pojo.Order;
<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.ibatis.annotations.Param;

public interface OrderMapper {

=======

public interface OrderMapper {
>>>>>>> b688ebfab920628eafa9c4ad1021c5673a1bae70
=======

public interface OrderMapper {
>>>>>>> 319770798c7ce1d95fcbbb1b1c2c241c5f673302
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);
<<<<<<< HEAD
<<<<<<< HEAD

    Order selectUserIdAndOrderId(@Param("id")Integer id, @Param("id") Long num);

    Order selectByorderNo(Long oderNo);
=======
>>>>>>> b688ebfab920628eafa9c4ad1021c5673a1bae70
=======
>>>>>>> 319770798c7ce1d95fcbbb1b1c2c241c5f673302
}