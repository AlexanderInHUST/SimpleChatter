package util;

/**
 * Created by tangyifeng on 2017/10/10.
 * Email: yifengtang_hust@outlook.com
 */
public class Const {

    // debug
    public static final boolean IS_DEBUG = false;

    // package const
    public static final long DEFAULT_TIMEOUT = 50;
    public static final int PACKAGE_LEN = 1024 * 16; // 10kb per pack
    public static final int WINDOW_WIDTH = 8;

    // three hello const
    public final static int RECEIVER = 0;
    public final static int SENDER = 1;


    //Stable UDP
    public final static int SEND_TIMEOUT = 10; // resend time out
    public final static int SEND_COUNT = 3000; // resend fail time

    public final static int RECV_TIMEOUT = 1000; // waiting pack time out

    // UDP
    public final static int UDP_BACK_PORT = 25555;
    public final static int UDP_SEND_PORT = 8999;
    public final static int UDP_POWER_BYTE = 1000;
    public final static int UDP_TIMEOUT = 500;

    public final static String SAMPLE_TEXT = "Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!";
}
