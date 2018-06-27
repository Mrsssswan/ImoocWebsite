package mrsssswan.mall.dao;

import mrsssswan.mall.pojo.Product;
<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.ibatis.annotations.Param;

import java.util.List;
=======
>>>>>>> b688ebfab920628eafa9c4ad1021c5673a1bae70
=======
import org.apache.ibatis.annotations.Param;

import java.util.List;
>>>>>>> 319770798c7ce1d95fcbbb1b1c2c241c5f673302

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 319770798c7ce1d95fcbbb1b1c2c241c5f673302

    List<Product> getList();

    List<Product> getListByNameAndId(@Param("name") String name, @Param("id") Integer id);
<<<<<<< HEAD
=======
>>>>>>> b688ebfab920628eafa9c4ad1021c5673a1bae70
=======
>>>>>>> 319770798c7ce1d95fcbbb1b1c2c241c5f673302
}