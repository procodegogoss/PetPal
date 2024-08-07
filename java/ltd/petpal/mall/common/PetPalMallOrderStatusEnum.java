package ltd.petpal.mall.common;

/**
 * @author 
 * @apiNote Order status: 0. Pending payment 1. Paid 2. Distribution completed 3: Delivery successful 
 * 4. Transaction successful -1. Manual closure -2. Timeout closure -3. Merchant closure
 */
public enum PetPalMallOrderStatusEnum {

    DEFAULT(-9, "ERROR"),
    ORDER_PRE_PAY(0, "Pending payment"),
    OREDER_PAID(1, "Paid"),
    OREDER_PACKAGED(2, "Distribution completed"),
    OREDER_EXPRESS(3, "Delivery successful"),
    ORDER_SUCCESS(4, "Transaction successful"),
    ORDER_CLOSED_BY_MALLUSER(-1, "Manual closure"),
    ORDER_CLOSED_BY_EXPIRED(-2, "Timeout closure"),
    ORDER_CLOSED_BY_JUDGE(-3, "Merchant closure");

    private int orderStatus;

    private String name;

    PetPalMallOrderStatusEnum(int orderStatus, String name) {
        this.orderStatus = orderStatus;
        this.name = name;
    }

    public static PetPalMallOrderStatusEnum getPetPalMallOrderStatusEnumByStatus(int orderStatus) {
        for (PetPalMallOrderStatusEnum PetPalMallOrderStatusEnum : PetPalMallOrderStatusEnum.values()) {
            if (PetPalMallOrderStatusEnum.getOrderStatus() == orderStatus) {
                return PetPalMallOrderStatusEnum;
            }
        }
        return DEFAULT;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
