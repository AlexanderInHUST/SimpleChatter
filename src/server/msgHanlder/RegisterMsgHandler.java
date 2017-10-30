package server.msgHanlder;

import message.Message;
import message.MessageCoder;
import server.sql.SqlHelper;
import server.sql.detail.SqlAccount;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import static message.MessageConst.ACC_MSG;

/**
 * Created by tangyifeng on 2017/10/29.
 * Email: yifengtang_hust@outlook.com
 */
public class RegisterMsgHandler implements IMsgHandler {

    @Override
    public void refresh() {

    }

    @Override
    public void handleMsg(byte[] data, SqlHelper sqlHelper, Socket socket) {
        ArrayList<String> detailData = MessageCoder.decode(new String(data));
        // account; password; public_key; question; answer
        SqlAccount sqlAccount = sqlHelper.getSqlAccount();
        sqlAccount.insertAccount(detailData.get(0),
                detailData.get(1),
                detailData.get(2),
                detailData.get(3),
                detailData.get(4));
        try {
            Message okMsg = new Message(ACC_MSG, "Complete");
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(okMsg);
            outputStream.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
