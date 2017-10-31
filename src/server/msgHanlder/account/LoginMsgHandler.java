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
import static message.MessageConst.CHECK_FAIL;
import static message.MessageConst.SUCCESS;

/**
 * Created by tangyifeng on 2017/10/29.
 * Email: yifengtang_hust@outlook.com
 */
public class LoginMsgHandler implements IMsgHandler {
    @Override
    public void refresh() {

    }

    @Override
    public void handleMsg(Message message, SqlHelper sqlHelper, Socket socket) {
        ArrayList<String> detailData = MessageCoder.decode(new String(message.getData()));
        //account;password
        SqlAccount sqlAccount = sqlHelper.getSqlAccount();
        boolean result = sqlAccount.checkPassword(detailData.get(0), detailData.get(1));
        try {
            Message okMsg = new Message(ACC_MSG, (result) ? SUCCESS : CHECK_FAIL);
            okMsg.setFromWho(detailData.get(0));
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(okMsg);
            outputStream.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
