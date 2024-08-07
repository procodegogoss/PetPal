package ltd.petpal.mall.common;

/**
 * @author 
 * @apiNote
 */
public class Constants {
    public final static String FILE_UPLOAD_DIC = "E:\\upload\\";//The default URL prefix for uploaded files, which can be modified according to the deployment settings.

    public final static int INDEX_CAROUSEL_NUMBER = 5;//The number of carousel images on the homepage (can be modified )

    public final static int INDEX_CATEGORY_NUMBER = 10;//The maximum number of first-level categories on the home page

    public final static int SEARCH_CATEGORY_NUMBER = 8;//The maximum number of first-level categories on a search page

    public final static int INDEX_GOODS_HOT_NUMBER = 4;//Number of hot-selling items on the home page
    public final static int INDEX_GOODS_NEW_NUMBER = 5;//Home page new product quantity
    public final static int INDEX_GOODS_RECOMMOND_NUMBER = 10;//Number of recommended products on the home page

    public final static int SHOPPING_CART_ITEM_TOTAL_NUMBER = 13;//The maximum number of items in the shopping cart (can be modified according to your own needs)

    public final static int SHOPPING_CART_ITEM_LIMIT_NUMBER = 5;//The maximum purchase quantity of a single item in the shopping cart (can be modified according to your own needs)

    public final static String MALL_VERIFY_CODE_KEY = "mallVerifyCode";//Verification code key

    public final static String MALL_USER_SESSION_KEY = "petPalMallUser";//session user key

    public final static int GOODS_SEARCH_PAGE_LIMIT = 10;//The default number of search pagination items (10 items per page)

    public final static int ORDER_SEARCH_PAGE_LIMIT = 5;//The default number of items in my order list pagination (5 items per page)

    public final static int SELL_STATUS_UP = 0;//Product shelf status
    public final static int SELL_STATUS_DOWN = 1;//Product off-shelf status

}
