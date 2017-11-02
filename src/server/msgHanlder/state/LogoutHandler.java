package server.msgHanlder.state;

import message.Message;
import message.MessageCoder;
import security.SecurityGuard;
import server.msgHanlder.IMsgHandler;
import server.sql.SqlHelper;
import server.sql.detail.SqlSecurity;
import server.sql.detail.SqlState;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import static message.MessageConst.*;
import static security.SecurityConst.SERVER_RSA;

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
        String fromWho = message.getFromWho();
        SqlSecurity sqlSecurity = sqlHelper.getSqlSecurity();
        String publicKey = sqlSecurity.getPublicKey(fromWho);
        SecurityGuard guard = new SecurityGuard(publicKey, SERVER_RSA);
        SqlState sqlState = sqlHelper.getSqlState();
        boolean result = sqlState.deleteLogin(fromWho);
        try {
            Message okMsg = new Message();
            okMsg.setKind(ACC_MSG);
            okMsg.setData(guard.encryptByPublicKey((result) ? BYE.getBytes() : ERROR.getBytes()));
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(okMsg);
            outputStream.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
