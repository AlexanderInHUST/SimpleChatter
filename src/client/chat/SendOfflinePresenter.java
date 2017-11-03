package client.chat;

import client.c2s.MessageSender;
import client.sql.SqlHelper;
import client.sql.detail.SqlSecurity;
import message.Message;
import security.SecurityGuard;

import static message.MessageConst.ACC_MSG;
import static message.MessageConst.SEND_OFFLINE_CHAT_MSG;
import static message.MessageConst.SUCCESS;

/**
 * Created by tangyifeng on 2017/11/3.
 * Email: yifengtang_hust@outlook.com
 */
public class SendOfflinePresenter {

    private MessageSender sender;
    private SqlHelper sqlHelper;

    public SendOfflinePresenter(SqlHelper sqlHelper) {
        this.sqlHelper = sqlHelper;
    }

    public boolean send(String fromWho, String toWho, String msg) {
        SqlSecurity sqlSecurity = sqlHelper.getSqlSecurity();
        String privateKey = sqlSecurity.getPrivateKey(fromWho);
        sender = new MessageSender(privateKey);
        Message message = getMsg(fromWho, toWho, msg);
        Message reply = sender.sendMessageSafely(message, fromWho);
        return reply.getKind() == ACC_MSG && !(new String(reply.getData()).equals(SUCCESS));
    }

    private Message getMsg(String fromWho, String toWho, String msg) {
        Message message = new Message();
        message.setKind(SEND_OFFLINE_CHAT_MSG);
        message.setToWho(toWho);
        message.setData(msg);
        return message;
    }

    public static void main(String[] args) {
        SqlHelper helper = new SqlHelper();
        SendOfflinePresenter sendOfflinePresenter = new SendOfflinePresenter(helper);
        sendOfflinePresenter.send("tyf", "myh", "hello1!");
        sendOfflinePresenter.send("tyf", "myh", "hello2!");
        sendOfflinePresenter.send("tyf", "myh", "hello3!");
        sendOfflinePresenter.send("tyf", "myh", "hello4!");
    }

}
