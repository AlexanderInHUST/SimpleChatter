package client.c2s.chat;

import client.c2s.MessageSender;
import client.sql.SqlHelper;
import client.sql.detail.SqlSecurity;
import message.Message;
import message.MessageCoder;

import java.util.ArrayList;

import static message.MessageConst.CHECK_FAIL;
import static message.MessageConst.CHECK_STATE_MSG;

/**
 * Created by tangyifeng on 2017/11/3.
 * Email: yifengtang_hust@outlook.com
 */
public class CheckState {

    private MessageSender sender;
    private SqlHelper sqlHelper;

    public CheckState(SqlHelper sqlHelper) {
        this.sqlHelper = sqlHelper;
    }

    public ArrayList<String> check(String fromWho, String account) {
        SqlSecurity sqlSecurity = sqlHelper.getSqlSecurity();
        String privateKey = sqlSecurity.getPrivateKey(fromWho);
        sender = new MessageSender(privateKey);
        Message message = getMsg(account);
        Message reply = sender.sendMessageSafely(message, fromWho);
        // CHECK_FAIL / ip;port
        String replyData = new String(reply.getData());
        if(replyData.equals(CHECK_FAIL)) {
            return null;
        }
        return MessageCoder.decode(replyData);
    }

    private Message getMsg(String account) {
        Message msg = new Message();
        msg.setKind(CHECK_STATE_MSG);
        msg.setData(account);
        return msg;
    }

    public static void main(String[] args) {
        SqlHelper helper = new SqlHelper();
        CheckState checkState = new CheckState(helper);
        ArrayList<String> arrayList = checkState.check("myh", "tyf");
        for (String s : arrayList) {
            System.out.println(s);
        }
    }
}
