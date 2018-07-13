package mrsssswan.mall.service;

import com.github.pagehelper.PageInfo;
import mrsssswan.mall.commons.ServerResponse;
import mrsssswan.mall.pojo.Shipping;

public interface IShippingService {
    ServerResponse add(Integer userid,Shipping shipping);

    ServerResponse<String> delete(Integer userid,Integer shippingId);

    ServerResponse<String> update(Integer id, Shipping shipping);

    ServerResponse<Shipping> select(Integer id, Integer shippingId);

    ServerResponse<PageInfo> list(Integer id, int pageNum, int pageSize);
}
