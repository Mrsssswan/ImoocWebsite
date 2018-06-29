package mrsssswan.mall.dao;
import com.google.common.collect.Lists;
import mrsssswan.mall.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import mrsssswan.mall.pojo.OrderItem;
import mrsssswan.mall.pojo.OrderItem;


public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);
    int updateByPrimaryKeySelective(OrderItem record);
    int updateByPrimaryKey(OrderItem record);

    List<OrderItem> getByOrderNoAndUserId(@Param("orderNo")Long orderNo,@Param("userId")Integer userId);

}