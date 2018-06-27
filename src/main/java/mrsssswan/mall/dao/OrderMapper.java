package mrsssswan.mall.dao;

import mrsssswan.mall.pojo.Order;
<<<<<<< HEAD
import org.apache.ibatis.annotations.Param;

public interface OrderMapper {

=======

public interface OrderMapper {
>>>>>>> b688ebfab920628eafa9c4ad1021c5673a1bae70
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);
<<<<<<< HEAD

    Order selectUserIdAndOrderId(@Param("id")Integer id, @Param("id") Long num);

    Order selectByorderNo(Long oderNo);
=======
>>>>>>> b688ebfab920628eafa9c4ad1021c5673a1bae70
}