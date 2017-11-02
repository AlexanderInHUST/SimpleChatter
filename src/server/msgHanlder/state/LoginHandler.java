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

import static message.MessageConst.ACC_MSG;
import static message.MessageConst.SUCCESS;
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
        ArrayList<String> detailData = MessageCoder.decode(decryptData);
        // account; ip_address; port
        SqlState sqlState = sqlHelper.getSqlState();
        sqlState.insertLogin(detailData.get(0),
                detailData.get(1),
                detailData.get(2));
        try {
            Message okMsg = new Message();
            okMsg.setKind(ACC_MSG);
            okMsg.setData(guard.encryptByPublicKey(SUCCESS.getBytes()));
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(okMsg);
            outputStream.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
