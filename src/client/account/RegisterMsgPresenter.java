package client.account;

import client.c2s.MessageSender;
import client.sql.SqlHelper;
import client.sql.detail.SqlAccount;
import message.Message;
import security.MD5Verify;
import security.SecurityGuard;

import static message.MessageConst.ACC_MSG;
import static message.MessageConst.REGISTER_MSG;
import static message.MessageConst.SUCCESS;

/**
 * Created by tangyifeng on 2017/10/30.
 * Email: yifengtang_hust@outlook.com
 */
public class RegisterMsgPresenter {

    private MessageSender sender;
    private SecurityGuard guard;
    private SqlHelper helper;

    public RegisterMsgPresenter(SqlHelper helper) {
        this.helper = helper;
    }

    public boolean register(String account, String password, String pwdQuestion, String pwdAnswer) {
        guard = new SecurityGuard();
        String privateKey = guard.getPrivateKey();
        String publicKey = guard.getPublicKey();
        sender = new MessageSender(privateKey);
        Message msg = getMsg(account, password, publicKey, pwdQuestion, pwdAnswer);
        Message reply = sender.sendMessageUnsafely(msg);
        if (reply.getKind() == ACC_MSG && new String(reply.getData()).equals(SUCCESS)) {
            storePrivateKey(account, privateKey);
            return true;
        } else {
            return false;
        }
    }

    private void storePrivateKey(String account, String key) {
        SqlAccount sqlAccount = helper.getSqlAccount();
        sqlAccount.insertAccount(account, key);
    }

    private Message getMsg(String account, String password, String publicKey, String pwdQuestion, String pwdAnswer) {
        Message msg = new Message();
        StringBuilder builder = new StringBuilder();
        builder.append(account)
                .append(";")
                .append(MD5Verify.getMd5(password))
                .append(";")
                .append(publicKey)
                .append(";")
                .append(MD5Verify.getMd5(pwdQuestion))
                .append(";")
                .append(MD5Verify.getMd5(pwdAnswer));
        msg.setKind(REGISTER_MSG);
        msg.setData(builder.toString());
        return msg;
    }

    public static void main(String[] args) {
        SqlHelper helper = new SqlHelper();
        RegisterMsgPresenter presenter = new RegisterMsgPresenter(helper);
        System.out.println(presenter.register("tyf", "123", "who i am", "tyf"));
    }
}
