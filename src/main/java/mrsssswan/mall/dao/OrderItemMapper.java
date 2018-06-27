package mrsssswan.mall.dao;

<<<<<<< HEAD
import com.google.common.collect.Lists;
import mrsssswan.mall.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;
=======
import mrsssswan.mall.pojo.OrderItem;
>>>>>>> b688ebfab920628eafa9c4ad1021c5673a1bae70

public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);
<<<<<<< HEAD

    List<OrderItem> getByOrderNoAndUserId(@Param("orderNo")Long orderNo,@Param("userId")Integer userId);
=======
>>>>>>> b688ebfab920628eafa9c4ad1021c5673a1bae70
}