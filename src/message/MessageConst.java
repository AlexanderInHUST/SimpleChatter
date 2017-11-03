package message;

/**
 * Created by tangyifeng on 2017/10/29.
 * Email: yifengtang_hust@outlook.com
 */
public class MessageConst {

    public static final int ACC_MSG = -1;

    // C2S
    // Server response MSG
    public static final String SUCCESS = "Complete!";
    public static final String CHECK_FAIL = "Fail!";
    public static final String ERROR = "Error!";
    public static final String HELLO = "Hello!";
    public static final String BYE = "Bye!";

    // Account MSG
    public static final int REGISTER_MSG = 0;
    public static final int LOGIN_MSG = 1;
    public static final int FORGET_PSWORD_MSG = 2;

    // State MSG
    public static final int LOG_IN_MSG = 3;
    public static final int LOG_OUT_MSG = 4;

    // Chat MSG
    public static final int CHECK_STATE_MSG = 5;
    public static final int ASK_OFFLINE_CHAT_MSG = 6;
    public static final int SEND_OFFLINE_CHAT_MSG = 7;

    // P2P
    // File transmit MSG
    public static final int FILE_WANNA_MSG = 100; // recv in msgQueue
    public static final int FILE_READY_MSG = 101;
    public static final int FILE_DONE_MSG = 102;
    public static final int FILE_OK_MSG = 103;

    // Chat MSG
    public static final int CHAT_SEND_MSG = 105;
}
