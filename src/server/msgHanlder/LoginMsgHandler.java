package server.msgHanlder;

import server.sql.SqlHelper;

import java.net.Socket;
import java.sql.Connection;

/**
 * Created by tangyifeng on 2017/10/29.
 * Email: yifengtang_hust@outlook.com
 */
public class LoginMsgHandler implements MsgHandler {
    @Override
    public void refresh() {

    }

    @Override
    public void handleMsg(String data, SqlHelper sqlHelper, Socket socket) {
        System.out.println("login");
    }
}
