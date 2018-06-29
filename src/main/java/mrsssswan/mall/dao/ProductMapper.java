package mrsssswan.mall.dao;
import mrsssswan.mall.pojo.Product;
import org.apache.ibatis.annotations.Param;
import java.util.List;


public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> getList();

    List<Product> getListByNameAndId(@Param("name") String name, @Param("id") Integer id);

}