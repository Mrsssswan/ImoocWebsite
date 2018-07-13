package mrsssswan.mall.dao;

import mrsssswan.mall.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    int deleteShipIpByUserId(@Param("userId") Integer userId,@Param("shippingId") Integer shippingId);

    int updateByUserIdAndShipping(Shipping shipping);

    Shipping selectShippingByUserIdShippingId(@Param("userId") Integer userId,@Param("shippingId") Integer shippingId);

    List<Shipping> selectShippingsByUserId(Integer userId);
}