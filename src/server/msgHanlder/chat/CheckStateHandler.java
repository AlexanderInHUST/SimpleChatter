package server.msgHanlder.chat;

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
 * Created by tangyifeng on 2017/11/2.
 * Email: yifengtang_hust@outlook.com
 */
public class CheckStateHandler implements IMsgHandler {
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
        // account;
        SqlState sqlState = sqlHelper.getSqlState();
        ArrayList<String> result = sqlState.checkOnline(detailData.get(0));
        String s = "";
        if (result != null) {
            s += result.get(0);
            s += ";";
            s += result.get(1);
        }

        try {
            Message okMsg = new Message();
            okMsg.setKind(ACC_MSG);
            okMsg.setData(guard.encryptByPublicKey((result != null) ? s.getBytes() : CHECK_FAIL.getBytes()));
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(okMsg);
            outputStream.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
