package security;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.DigestInputStream;
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

    public static String getFileMD5(String fileName) {
        try {
            if (messageDigest == null) {
                messageDigest = MessageDigest.getInstance("MD5");
            } else {
                messageDigest.reset();
            }
            File file = new File(fileName);
            if (file.exists()) {
                FileInputStream inputStream = new FileInputStream(file);
                DigestInputStream digestInputStream = new DigestInputStream(inputStream, messageDigest);
                int bufSize = 256 * 1024;
                byte[] buf = new byte[bufSize];
                while (digestInputStream.read(buf) > 0);
                messageDigest = digestInputStream.getMessageDigest();
                BigInteger bigInt = new BigInteger(1, messageDigest.digest());
                return bigInt.toString(16);
            } else {
                return null;
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static void main(String[] args) {
//        String md51 = getFileMD5("/Users/tangyifeng/Desktop/text.cpp");
//        System.out.println(md51);
//    }

}
