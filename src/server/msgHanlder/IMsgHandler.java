package server.msgHanlder;

import server.sql.SqlHelper;

import java.net.Socket;

/**
 * Created by tangyifeng on 2017/10/29.
 * Email: yifengtang_hust@outlook.com
 */
public interface IMsgHandler {

    void refresh();
    void handleMsg(byte[] data, SqlHelper sqlHelper, Socket socket);

}
