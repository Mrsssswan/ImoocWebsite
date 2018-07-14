package mrsssswan.mall.vo;

import java.math.BigDecimal;
import java.util.List;

public class OrderProductVO {
   private List<OrderItemVo> orderItemList;
   private BigDecimal totlePrice;
   private String imageHost;

    public List<OrderItemVo> getOrderItemList() {

        return orderItemList;
    }

    public void setOrderItemList(List<OrderItemVo> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public BigDecimal getTotlePrice() {
        return totlePrice;
    }

    public void setTotlePrice(BigDecimal totlePrice) {
        this.totlePrice = totlePrice;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
