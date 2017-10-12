package util;

/**
 * Created by tangyifeng on 2017/10/13.
 * Email: yifengtang_hust@outlook.com
 */
public class Log {

    public static void log(String className, String detail) {
        if (Const.IS_DEBUG) {
            System.out.println(className + ": " + detail);
        }
    }

}
