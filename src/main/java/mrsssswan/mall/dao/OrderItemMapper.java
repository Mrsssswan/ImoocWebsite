package mrsssswan.mall.dao;

<<<<<<< HEAD
<<<<<<< HEAD
import com.google.common.collect.Lists;
import mrsssswan.mall.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;
=======
import mrsssswan.mall.pojo.OrderItem;
>>>>>>> b688ebfab920628eafa9c4ad1021c5673a1bae70
=======
import mrsssswan.mall.pojo.OrderItem;
>>>>>>> 319770798c7ce1d95fcbbb1b1c2c241c5f673302

public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);
<<<<<<< HEAD
<<<<<<< HEAD

    List<OrderItem> getByOrderNoAndUserId(@Param("orderNo")Long orderNo,@Param("userId")Integer userId);
=======
>>>>>>> b688ebfab920628eafa9c4ad1021c5673a1bae70
=======
>>>>>>> 319770798c7ce1d95fcbbb1b1c2c241c5f673302
}