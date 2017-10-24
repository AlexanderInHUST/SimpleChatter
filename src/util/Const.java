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
    public static final int PACKAGE_LEN = 2;
    public static final int WINDOW_WIDTH = 20;

    // three hello const
    public final static int RECEIVER = 0;
    public final static int SENDER = 1;


    //Stable UDP
    public final static int SEND_TIMEOUT = 20;
    public final static int SEND_COUNT = 10;

    public final static int RECV_TIMEOUT = 500;

    // UDP
    public final static int UDP_BACK_PORT = 8888;
    public final static int UDP_SEND_PORT = 8999;
    public final static int UDP_POWER_BYTE = 1000;
    public final static int UDP_TIMEOUT = 500;
}
