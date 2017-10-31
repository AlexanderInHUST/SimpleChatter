package server.msgHanlder.state;

import message.Message;
import server.msgHanlder.IMsgHandler;
import server.sql.SqlHelper;

import java.net.Socket;

/**
 * Created by tangyifeng on 2017/10/31.
 * Email: yifengtang_hust@outlook.com
 */
public class LogoutHandler implements IMsgHandler {
    @Override
    public void refresh() {

    }

    @Override
    public void handleMsg(Message message, SqlHelper sqlHelper, Socket socket) {

    }
}
