package client.p2p.server.msgHandler;

import message.Message;

import java.net.Socket;

/**
 * Created by tangyifeng on 2017/11/3.
 * Email: yifengtang_hust@outlook.com
 */
public interface IMsgHandler {

    void refresh();
    void handleMsg(Message message, Socket socket);
    void setCallback(IMsgCallback callback);

}
