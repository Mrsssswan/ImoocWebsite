package mrsssswan.mall.vo;

import java.math.BigDecimal;
import java.util.List;

public class CartVO {
    private List<CartProductVO> cartProductVOS;
    private BigDecimal totlePrice;
    private Boolean allChecked;
    private String imageHost;

    public List<CartProductVO> getCartProductVOS() {
        return cartProductVOS;
    }

    public void setCartProductVOS(List<CartProductVO> cartProductVOS) {
        this.cartProductVOS = cartProductVOS;
    }

    public BigDecimal getTotlePrice() {
        return totlePrice;
    }

    public void setTotlePrice(BigDecimal totlePrice) {
        this.totlePrice = totlePrice;
    }

    public Boolean getAllChecked() {
        return allChecked;
    }

    public void setAllChecked(Boolean allChecked) {
        this.allChecked = allChecked;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
