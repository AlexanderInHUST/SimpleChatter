package server.msgHanlder;

import message.Message;
import message.MessageCoder;
import server.sql.SqlHelper;
import server.sql.detail.SqlAccount;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Created by tangyifeng on 2017/10/29.
 * Email: yifengtang_hust@outlook.com
 */
public class RegisterMsgHandler implements MsgHandler {



    @Override
    public void refresh() {

    }

    @Override
    public void handleMsg(String data, SqlHelper sqlHelper) {
        ArrayList<String> detailData = MessageCoder.decode(data);
        // account; password; public_key; question; answer
        SqlAccount sqlAccount = sqlHelper.getSqlAccount();
        sqlAccount.insertAccount(detailData.get(0),
                detailData.get(1),
                detailData.get(2),
                detailData.get(3),
                detailData.get(4));
    }
}
