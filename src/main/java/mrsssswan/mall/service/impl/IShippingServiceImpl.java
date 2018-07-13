package mrsssswan.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import mrsssswan.mall.commons.ServerResponse;
import mrsssswan.mall.dao.ShippingMapper;
import mrsssswan.mall.pojo.Shipping;
import mrsssswan.mall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("iShippingService")
public class IShippingServiceImpl implements IShippingService {
    @Autowired
    private ShippingMapper shippingMapper;
    @Override
    public ServerResponse add(Integer userid, Shipping shipping) {
        shipping.setUserId(userid);
        int row = shippingMapper.insert(shipping);
        if(row>0){
            Map reaultMap = Maps.newHashMap();
            reaultMap.put("shippingId",shipping.getId());
            return ServerResponse.createBySuccess("新建地址成功",reaultMap);
        }
        return ServerResponse.createByErrorMessage("新建地址失败");
    }

    @Override
    public ServerResponse<String> delete(Integer userid, Integer shippingId) {
         int row = shippingMapper.deleteShipIpByUserId(userid,shippingId);
        if(row>0)
            return ServerResponse.createBySuccess("删除地址成功");

        return ServerResponse.createByErrorMessage("删除地址失败");
    }

    @Override
    public ServerResponse<String> update(Integer userid, Shipping shipping) {
        shipping.setUserId(userid);
        int row = shippingMapper.updateByUserIdAndShipping(shipping);
        if(row>0){
            return ServerResponse.createBySuccess("更新地址成功");
        }
        return ServerResponse.createByErrorMessage("更新地址失败");
    }

    @Override
    public ServerResponse<Shipping> select(Integer id, Integer shippingId) {
        Shipping shipping = shippingMapper.selectShippingByUserIdShippingId(id,shippingId);
        if(shipping == null){
            return ServerResponse.createByErrorMessage("无法查询该地址");
        }
        return ServerResponse.createBySuccess(shipping);
    }

    @Override
    public ServerResponse<PageInfo> list(Integer id, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.selectShippingsByUserId(id);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
