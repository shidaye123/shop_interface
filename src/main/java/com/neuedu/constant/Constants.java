package com.neuedu.constant;

public class Constants {

    public static final String CURRENT_USER = "user";

    public static final String CURRENT_CATEGORIES = "categories";


    /**
     * 枚举常量
     *
     * */
    public static enum productCheckedEnum{

        PRODUCT_CHECKED(1,"勾选"),
        PRODUCT_UNCHECKED(0,"未勾选");

        private final Integer code;
        private final String checkedMsg;

        private productCheckedEnum(Integer code,String checkedMsg){

            this.code = code;
            this.checkedMsg = checkedMsg;

        }

        public Integer getCode() {
            return code;
        }

        public String getCheckedMsg() {
            return checkedMsg;
        }
    }

    //订单状态
    public static enum orderStatusEnum{

        ORDER_CANCEL(0,"已取消"),
        ORDER_UNPAY(10,"未支付"),
        ORDER_PAY(20,"已支付"),
        ORDER_SEND(40,"已发货"),
        ORDER_SUCCESSFUL_TRADE(50,"交易成功"),
        ORDER_UNSUCCESSFUL_TRADE(60,"交易关闭");

        private final Integer code;
        private final String orderStatusMsg;

        private orderStatusEnum(Integer code,String orderStatusMsg){
            this.code = code;
            this.orderStatusMsg = orderStatusMsg;
        }

        public Integer getCode() {
            return code;
        }

        public String getOrderStatusMsg() {
            return orderStatusMsg;
        }

        public static orderStatusEnum codeof(Integer code){
            for (orderStatusEnum orderStatusEnum:values()){
                if (code==orderStatusEnum.getCode()){
                    return orderStatusEnum;
                }
            }
            return null;
        }
    }

    //商品状态
    public static enum productStatusEnum{

        PRODUCT_ONLINE(1,"在售"),
        PRODUCT_UNONLINE(2,"下架"),
        PRODUCT_DELETEED(3,"删除");

        private final Integer code;
        private final String productStatusMsg;

        private productStatusEnum(Integer code,String productStatusMsg){
            this.code = code;
            this.productStatusMsg = productStatusMsg;
        }

        public Integer getCode() {
            return code;
        }

        public String getProductStatusMsg() {
            return productStatusMsg;
        }
    }

    //支付类型
    public static enum paymentTypeEnum{

        PAYMENT_ONLINE(1,"在线支付");

        private final Integer code;
        private final String paymentTypeMsg;

        private paymentTypeEnum(Integer code,String paymentTypeMsg){
            this.code = code;
            this.paymentTypeMsg = paymentTypeMsg;
        }

        public Integer getCode() {
            return code;
        }

        public String getPaymentTypeMsg() {
            return paymentTypeMsg;
        }

        public static paymentTypeEnum codeof(Integer code){
            for (paymentTypeEnum paymentTypeEnum:values()){
                if (code==paymentTypeEnum.getCode()){
                    return paymentTypeEnum;
                }
            }
            return null;
        }
    }

    //支付平台枚举
    public static enum payPlatformEnum{

        PLATFORM_ALIPAY(1,"支付宝"),
        PLATFORM_WECHAT(2,"微信");

        private final Integer code;
        private final String platformMsg;

        private payPlatformEnum(Integer code,String platformMsg){
            this.code = code;
            this.platformMsg = platformMsg;
        }

        public Integer getCode() {
            return code;
        }

        public String getPlatformMsg() {
            return platformMsg;
        }
    }
}
