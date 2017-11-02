package client.state;

import client.c2s.MessageSender;
import client.sql.SqlHelper;
import client.sql.detail.SqlSecurity;
import message.Message;

import static message.MessageConst.*;

/**
 * Created by tangyifeng on 2017/10/31.
 * Email: yifengtang_hust@outlook.com
 */
public class LogoutPresenter {

    private MessageSender sender;
    private SqlHelper helper;

    public LogoutPresenter(SqlHelper helper) {
        this.helper = helper;
    }

    public boolean logout(String account) {
        SqlSecurity sqlSecurity = helper.getSqlSecurity();
        String privateKey = sqlSecurity.getPrivateKey(account);
        sender = new MessageSender(privateKey);
        Message msg = getMsg();
        Message reply = sender.sendMessageSafely(msg, account);
        return reply.getKind() == ACC_MSG && new String(reply.getData()).equals(SUCCESS);
    }

    private Message getMsg() {
        Message msg = new Message();
        msg.setKind(LOG_OUT_MSG);
        msg.setData("");
        return msg;
    }

    public static void main(String[] args) {
        SqlHelper helper = new SqlHelper();
        LogoutPresenter presenter = new LogoutPresenter(helper);
        boolean result = presenter.logout("tyf");
        System.out.println(result);
    }

}
