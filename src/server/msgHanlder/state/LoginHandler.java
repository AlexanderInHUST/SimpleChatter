package server.msgHanlder.state;

import message.Message;
import security.SecurityGuard;
import server.msgHanlder.IMsgHandler;
import server.sql.SqlHelper;
import server.sql.detail.SqlSecurity;

import java.net.Socket;

import static security.SecurityConst.SERVER_RSA;

/**
 * Created by tangyifeng on 2017/10/31.
 * Email: yifengtang_hust@outlook.com
 */
public class LoginHandler implements IMsgHandler {
    @Override
    public void refresh() {

    }

    @Override
    public void handleMsg(Message message, SqlHelper sqlHelper, Socket socket) {
        String fromWho = message.getFromWho();
        SqlSecurity sqlSecurity = sqlHelper.getSqlSecurity();
        String publicKey = sqlSecurity.getPublicKey(fromWho);
        SecurityGuard guard = new SecurityGuard(publicKey, SERVER_RSA);
        String decryptData = new String(guard.decryptByPublicKey(message.getSigns(), message.getData()));

    }
}
