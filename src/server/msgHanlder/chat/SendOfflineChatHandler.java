package server.msgHanlder.chat;

import message.Message;
import security.SecurityGuard;
import server.msgHanlder.IMsgHandler;
import server.sql.SqlHelper;
import server.sql.detail.SqlChat;
import server.sql.detail.SqlSecurity;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static message.MessageConst.ACC_MSG;
import static message.MessageConst.CHECK_FAIL;
import static message.MessageConst.SUCCESS;
import static security.SecurityConst.SERVER_RSA;

/**
 * Created by tangyifeng on 2017/11/3.
 * Email: yifengtang_hust@outlook.com
 */
public class SendOfflineChatHandler implements IMsgHandler {
    @Override
    public void refresh() {

    }

    @Override
    public void handleMsg(Message message, SqlHelper sqlHelper, Socket socket) {
        String fromWho = message.getFromWho();
        String toWho = message.getToWho();
        SqlChat sqlChat = sqlHelper.getSqlChat();
        SqlSecurity sqlSecurity = sqlHelper.getSqlSecurity();
        String publicKey = sqlSecurity.getPublicKey(fromWho);
        SecurityGuard guard = new SecurityGuard(publicKey, SERVER_RSA);
        boolean result = sqlChat.insertOfflineMsg(fromWho, toWho, message.getData());

        try {
            Message okMsg = new Message();
            okMsg.setKind(ACC_MSG);
            okMsg.setData(guard.encryptByPublicKey((result) ? SUCCESS.getBytes() : CHECK_FAIL.getBytes()));
            okMsg.setFromWho(fromWho);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(okMsg);
            outputStream.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
