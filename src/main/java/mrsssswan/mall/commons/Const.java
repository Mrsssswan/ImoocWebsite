package mrsssswan.mall.commons;

public class Const {

    public static final String CURRENT_USER = "current_user";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public interface Role{
        int ROLE_CUSTOMER = 0; //普通用户
        int ROLE_ADMIN = 1; //管理员
    }
<<<<<<< HEAD
<<<<<<< HEAD






   public  enum OrderStatusEnum{
        CANCEL(0,"已取消"),
        NO_PAY(10,"未支付"),
        PAID(20,"已支付"),
        SHIPPING(40,"已发货"),
       ORDER_SUCCESS(50,"订单完成"),
       ORDER_CLOSED(60,"订单关闭")
       ;

       OrderStatusEnum(int code, String value) {
           this.code = code;
           this.value = value;
       }

       private int code;
        private String value;

       public int getCode() {
           return code;
       }

       public String getValue() {
           return value;
       }
   }

   public interface AlipayCallback{
        String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        String TRADE_STATUS_TRADE_SUCCESS= "TRADE_SUCCESS";
       String RESPONSE_SUCCESS = "success";
       String RESPONSE_FAILED = "failed";
   }
=======
>>>>>>> b688ebfab920628eafa9c4ad1021c5673a1bae70
=======
>>>>>>> 319770798c7ce1d95fcbbb1b1c2c241c5f673302
}
