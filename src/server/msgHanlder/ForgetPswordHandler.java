package server.msgHanlder;

import message.Message;
import message.MessageCoder;
import server.sql.SqlHelper;
import server.sql.detail.SqlAccount;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import static message.MessageConst.*;

/**
 * Created by tangyifeng on 2017/10/29.
 * Email: yifengtang_hust@outlook.com
 */
public class ForgetPswordHandler implements IMsgHandler {

    @Override
    public void refresh() {

    }

    @Override
    public void handleMsg(Message message, SqlHelper sqlHelper, Socket socket) {
        ArrayList<String> detailData = MessageCoder.decode(new String(message.getData()));
        //account;passwordQue;passwordAns;newPassword
        SqlAccount sqlAccount = sqlHelper.getSqlAccount();
        boolean checkResult = sqlAccount.checkPwdAnswer(detailData.get(0), detailData.get(1), detailData.get(2));
        boolean editResult = false;
        if (checkResult) {
            editResult = sqlAccount.editPwd(detailData.get(0), detailData.get(3));
        }
        try {
            Message okMsg = new Message(ACC_MSG, (!checkResult) ? CHECK_FAIL : (editResult) ? SUCCESS : ERROR);
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
