package util;

/**
 * Created by tangyifeng on 2017/10/10.
 * Email: yifengtang_hust@outlook.com
 */
public class Const {

    // debug
    public static final boolean IS_DEBUG = true;

    // package const
    public static final long DEFAULT_TIMEOUT = 10;
    public static final int PACKAGE_LEN = 1;
    public static final int WINDOW_WIDTH = 5;

    // three hello const
    public final static int RECEIVER = 0;
    public final static int SENDER = 1;


    //Stable UDP
    public final static int SEND_TIMEOUT = 200;
    public final static int SEND_COUNT = 50;

    public final static int RECV_TIMEOUT = 1000;

    // UDP
    public final static int UDP_BACK_PORT = 8888;
    public final static int UDP_SEND_PORT = 8999;
    public final static int UDP_POWER_BYTE = 1000;
    public final static int UDP_TIMEOUT = 500;

    public final static String SAMPLE_TEXT = "Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!";
}
