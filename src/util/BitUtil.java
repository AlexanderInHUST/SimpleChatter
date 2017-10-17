package util;

/**
 * Created by tangyifeng on 2017/10/17.
 * Email: yifengtang_hust@outlook.com
 */
public class BitUtil {

    public static byte[] toByteArray(int iSource) {
        byte[] bLocalArr = new byte[16];
        for (int i = 0; (i < 4) && (i < 16); i++) {
            bLocalArr[i] = (byte) (iSource >> 8 * i & 0xFF);
        }
        return bLocalArr;
    }

    public static int toInt(byte[] bRefArr) {
        int iOutcome = 0;
        byte bLoop;

        for (int i = 0; i < bRefArr.length; i++) {
            bLoop = bRefArr[i];
            iOutcome += (bLoop & 0xFF) << (8 * i);
        }
        return iOutcome;
    }


}
