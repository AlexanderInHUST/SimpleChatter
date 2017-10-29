package server.msgHanlder;

/**
 * Created by tangyifeng on 2017/10/29.
 * Email: yifengtang_hust@outlook.com
 */
public interface MsgHandler {

    void refresh();
    void handleMsg(String data);

}
