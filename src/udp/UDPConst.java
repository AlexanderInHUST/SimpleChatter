package udp;

/**
 * Created by tangyifeng on 2017/10/10.
 * Email: yifengtang_hust@outlook.com
 */
public class UDPConst {

    // package const
    public static final long DEFAULT_TIMEOUT = 500;
    public static final int PACKAGE_LEN = 1024 * 60; // 60kb per pack
    public static final int WINDOW_WIDTH = 8;

    //Stable UDP
    public final static int SEND_WAIT = 17;
    public final static int SEND_TIMEOUT = 23; // resend time out
    public final static int SEND_COUNT = 20000; // resend fail time

    public final static int RECV_TIMEOUT = 3000; // waiting pack time out

    // UDP
    public final static int UDP_BACK_PORT = 25555;
    public final static int UDP_POWER_BYTE = 1;

    public final static String SAMPLE_TEXT = "Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!Hello world!";

}
