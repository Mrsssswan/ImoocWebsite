package mrsssswan.mall.dao;

import mrsssswan.mall.pojo.Order;
import mrsssswan.mall.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    Order selectUserIdAndOrderId(@Param("id")Integer id, @Param("num") Long num);

    Order selectByorderNo(Long oderNo);

}