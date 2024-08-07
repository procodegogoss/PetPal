package ltd.petpal.mall.common;

/**
 * @author 
 * @apiNote Order status: 0. None 1. CreditCard 2. Paypal Pay
 */
public enum PayTypeEnum {

    DEFAULT(-1, "ERROR"),
    NOT_PAY(0, "None"),
    CC_PAY(1, "CreditCard"),
    PAYPAL_PAY(2, "Paypal Pay");

    private int payType;

    private String name;

    PayTypeEnum(int payType, String name) {
        this.payType = payType;
        this.name = name;
    }

    public static PayTypeEnum getPayTypeEnumByType(int payType) {
        for (PayTypeEnum payTypeEnum : PayTypeEnum.values()) {
            if (payTypeEnum.getPayType() == payType) {
                return payTypeEnum;
            }
        }
        return DEFAULT;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
