package client.state;

import client.c2s.MessageSender;
import client.sql.SqlHelper;
import client.sql.detail.SqlSecurity;
import message.Message;
import security.SecurityGuard;

import java.security.Signature;
import java.util.ArrayList;

import static message.MessageConst.ACC_MSG;
import static message.MessageConst.LOG_IN_MSG;
import static message.MessageConst.SUCCESS;
import static security.SecurityConst.CLIENT_RSA;

/**
 * Created by tangyifeng on 2017/10/31.
 * Email: yifengtang_hust@outlook.com
 */
public class LoginPresenter  {

    private MessageSender sender;
    private SqlHelper helper;

    public LoginPresenter(SqlHelper helper) {
        this.helper = helper;
    }

    public boolean login(String account, String host, int port) {
        SqlSecurity sqlSecurity = helper.getSqlSecurity();
        String privateKey = sqlSecurity.getPrivateKey(account);
        sender = new MessageSender(privateKey);
        String data = account + ";" + host + ";" + Integer.toString(port);
        Message msg = getMsg(data.getBytes());
        Message reply = sender.sendMessageSafely(msg, account);
        return reply.getKind() == ACC_MSG && new String(reply.getData()).equals(SUCCESS);
    }

    private Message getMsg(byte[] encryptData) {
        Message msg = new Message();
        msg.setKind(LOG_IN_MSG);
        msg.setData(encryptData);
        return msg;
    }

    public static void main(String[] args) {
        SqlHelper helper = new SqlHelper();
        LoginPresenter presenter = new LoginPresenter(helper);
        boolean result = presenter.login("myh", "1.1.1.1", 123);
        System.out.println(result);
    }

}
