package util;

import java.io.*;

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

    public static byte[] fileToByteArray(String fileName) throws IOException {
        FileInputStream inputStream = new FileInputStream(fileName);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(4096);
        int n;
        byte[] b = new byte[4096];
        while ((n = inputStream.read(b)) != -1) {
            outputStream.write(b, 0, n);
        }
        inputStream.close();
        outputStream.close();
        return outputStream.toByteArray();
    }

    public static String byteArrayToFile(byte[] array, String fileName) throws IOException {
        File file = new File(fileName);
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
        outputStream.write(array);
        outputStream.close();
        return fileName;
    }

}
