package ltd.petpal.mall.util;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * @author 
 */
public class SystemUtil {

    private SystemUtil() {
    }


    /**
     * After successful login or registration, a session token value is generated to keep the user logged in.
     *
     * @param src:Now()+user.id+random(4) for the user’s latest login
     * @return
     */
    public static String genToken(String src) {
        if (null == src || "".equals(src)) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(src.getBytes());
            String result = new BigInteger(1, md.digest()).toString(16);
            if (result.length() == 31) {
                result = result + "-";
            }
            System.out.println(result);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

}
