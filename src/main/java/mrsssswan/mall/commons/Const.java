package mrsssswan.mall.commons;

public class Const {

    public static final String CURRENT_USER = "current_user";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public interface Role{
        int ROLE_CUSTOMER = 0; //普通用户
        int ROLE_ADMIN = 1; //管理员
    }

   public interface Cart{
       int CKECKED = 1;
       int UNCKECKED = 0;
       String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
       String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }

    public enum ProductStatus{

        On_SALE(1,"在售"),
        UNDER_SALE(0,"下架"),
        DELETE(-1,"已删除");
        int code;
        String msg;

        ProductStatus(int code,String msg){
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
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
       public static OrderStatusEnum codeToMsg(int code){
           for(OrderStatusEnum OrderStatusEnum:values()){
               if(OrderStatusEnum.getCode() == code)
                   return OrderStatusEnum;
           }
           throw new RuntimeException("没有找到对应的枚举");
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
    public enum payPlatformEnum{

       ALIPAY(1,"支付宝");

        payPlatformEnum(int code, String value) {
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
   public enum PaymentTypeEnum{

        OnLINE_PAY(1,"在线支付");

       PaymentTypeEnum(int code, String value) {
           this.code = code;
           this.value = value;
       }

       public static PaymentTypeEnum codeToMsg(int code){
           for(PaymentTypeEnum paymentTypeEnum:values()){
               if(paymentTypeEnum.getCode() == code)
                   return paymentTypeEnum;
           }
           throw new RuntimeException("没有找到对应的枚举");
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
}
