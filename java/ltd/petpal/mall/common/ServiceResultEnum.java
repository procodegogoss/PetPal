package ltd.petpal.mall.common;

/**
 * @author 13
 */
public enum ServiceResultEnum {
    ERROR("error"),

    SUCCESS("success"),

    DATA_NOT_EXIST("No record found！"),

    SAME_CATEGORY_EXIST("Categories with the same name and the same level！"),

    SAME_LOGIN_NAME_EXIST("Username already exists！"),

    LOGIN_NAME_NULL("Please enter your login name"),

    LOGIN_PASSWORD_NULL("Please enter password"),

    LOGIN_VERIFY_CODE_NULL("please enter verification code"),

    LOGIN_VERIFY_CODE_ERROR("Verification code error！"),

    GOODS_NOT_EXIST("The product does not exist!"),

    SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR("Maximum purchase quantity for a single item exceeded!"),

    LOGIN_ERROR("Login failed!"),

    LOGIN_USER_LOCKED("User locked"),

    ORDER_NOT_EXIST_ERROR("The order does not exist!"),

    NULL_ADDRESS_ERROR("The address cannot be empty!"),

    ORDER_PRICE_ERROR("The order price is abnormal!"),

    ORDER_GENERATE_ERROR("An order exception is generated!"),

    SHOPPING_ITEM_ERROR("Shopping cart is abnormal!"),

    SHOPPING_ITEM_COUNT_ERROR("Inventory shortage!"),

    ORDER_STATUS_ERROR("order status is abnormal!"),

    OPERATE_ERROR("Operation error"),

    DB_ERROR("database error");

    private String result;

    ServiceResultEnum(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
