package security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by tangyifeng on 2017/10/30.
 * Email: yifengtang_hust@outlook.com
 */
public class MD5Verify {

    private static MessageDigest messageDigest;

    public static String getMd5(String data) {
        try {
            if (messageDigest == null) {
                messageDigest = MessageDigest.getInstance("MD5");
            } else {
                messageDigest.reset();
            }
            messageDigest.update(data.getBytes());
            BigInteger bigInt = new BigInteger(1, messageDigest.digest());
            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String result1 = getMd5("hello world!");
        String result2 = getMd5("hello world!");
        System.out.println(result1.equals(result2));
    }

}
