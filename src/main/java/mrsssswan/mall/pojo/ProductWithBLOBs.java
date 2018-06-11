package mrsssswan.mall.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class ProductWithBLOBs extends Product {
    private String VARCHAR;

    private String VARCHAR;

    public ProductWithBLOBs(Integer id, Integer categoryId, String name, String subtitle, String mainImage, BigDecimal price, Integer stock, Integer status, Date createTime, Date updateTime, String VARCHAR, String VARCHAR) {
        super(id, categoryId, name, subtitle, mainImage, price, stock, status, createTime, updateTime);
        this.VARCHAR = VARCHAR;
        this.VARCHAR = VARCHAR;
    }

    public ProductWithBLOBs() {
        super();
    }

    public String getVARCHAR() {
        return VARCHAR;
    }

    public void setVARCHAR(String VARCHAR) {
        this.VARCHAR = VARCHAR == null ? null : VARCHAR.trim();
    }

    public String getVARCHAR() {
        return VARCHAR;
    }

    public void setVARCHAR(String VARCHAR) {
        this.VARCHAR = VARCHAR == null ? null : VARCHAR.trim();
    }
}