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
import java.util.ArrayList;
import java.util.HashMap;

import static message.MessageConst.ACC_MSG;
import static message.MessageConst.ERROR;
import static security.SecurityConst.SERVER_RSA;

/**
 * Created by tangyifeng on 2017/11/3.
 * Email: yifengtang_hust@outlook.com
 */
public class AskForOfflineMsgHandler implements IMsgHandler {

    @Override
    public void refresh() {

    }

    @Override
    public void handleMsg(Message message, SqlHelper sqlHelper, Socket socket) {
        String fromWho = message.getFromWho();
        SqlSecurity sqlSecurity = sqlHelper.getSqlSecurity();
        String publicKey = sqlSecurity.getPublicKey(fromWho);
        SecurityGuard guard = new SecurityGuard(publicKey, SERVER_RSA);
        SqlChat sqlChat = sqlHelper.getSqlChat();

        String offline = "";
        ArrayList<HashMap<String, Object>> result = sqlChat.getOfflineMsg(fromWho);
        boolean queryResult = !(result == null);
        if (queryResult) {
            for (HashMap<String, Object> map : result) {
                String fWho = (String) map.get("fromWho");
                SecurityGuard singleGuard = new SecurityGuard(sqlSecurity.getPublicKey(fWho), SERVER_RSA);
                String chatMsg = new String(singleGuard.decryptByPublicKeyWithoutSigns((byte[]) map.get("whatMsg")));
                offline += fWho;
                offline += ";";
                offline += chatMsg;
                offline += ";";
            }
        }

        try {
            Message okMsg = new Message();
            okMsg.setKind(ACC_MSG);
            okMsg.setData(guard.encryptByPublicKey((queryResult) ? offline.getBytes() : ERROR.getBytes()));
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(okMsg);
            outputStream.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
