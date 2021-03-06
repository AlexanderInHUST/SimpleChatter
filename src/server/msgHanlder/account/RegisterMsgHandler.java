package server.msgHanlder.account;

import message.Message;
import message.MessageCoder;
import server.msgHanlder.IMsgHandler;
import server.sql.SqlHelper;
import server.sql.detail.SqlAccount;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import static message.MessageConst.ACC_MSG;
import static message.MessageConst.ERROR;
import static message.MessageConst.SUCCESS;

/**
 * Created by tangyifeng on 2017/10/29.
 * Email: yifengtang_hust@outlook.com
 */
public class RegisterMsgHandler implements IMsgHandler {

    @Override
    public void refresh() {

    }

    @Override
    public void handleMsg(Message message, SqlHelper sqlHelper, Socket socket) {
        ArrayList<String> detailData = MessageCoder.decode(new String(message.getData()));
        // account; password; public_key; question; answer
        SqlAccount sqlAccount = sqlHelper.getSqlAccount();
        boolean result = sqlAccount.insertAccount(detailData.get(0),
                detailData.get(1),
                detailData.get(2),
                detailData.get(3),
                detailData.get(4));
        try {
            Message okMsg = new Message(ACC_MSG, (result) ? SUCCESS : ERROR);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(okMsg);
            outputStream.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
