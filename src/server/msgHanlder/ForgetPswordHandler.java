package server.msgHanlder;

import server.sql.SqlHelper;

import java.net.Socket;

/**
 * Created by tangyifeng on 2017/10/29.
 * Email: yifengtang_hust@outlook.com
 */
public class ForgetPswordHandler implements IMsgHandler {
    @Override
    public void refresh() {

    }

    @Override
    public void handleMsg(byte[] data, SqlHelper sqlHelper, Socket socket) {
        System.out.println("forget");
    }
}
