package client.account;

import client.c2s.MessageSender;
import message.Message;
import security.MD5Verify;
import security.SecurityGuard;

import static message.MessageConst.ACC_MSG;
import static message.MessageConst.REGISTER_MSG;

/**
 * Created by tangyifeng on 2017/10/30.
 * Email: yifengtang_hust@outlook.com
 */
public class RegisterPresenter {

    private MessageSender sender;
    private SecurityGuard guard;

    public boolean register(String account, String password, String pwdQuestion, String pwdAnswer) {
        guard = new SecurityGuard();
        String privateKey = guard.getPrivateKey();
        String publicKey = guard.getPublicKey();
        storePrivateKey(privateKey);
        sender = new MessageSender(privateKey);
        Message msg = getMsg(account, password, publicKey, pwdQuestion, pwdAnswer);
        Message reply = sender.sendMessageUnsafely(msg);
        return reply.getKind() == ACC_MSG && new String(reply.getData()).equals("Complete");
    }

    private void storePrivateKey(String key) {
        System.out.println(key);
    }

    private Message getMsg(String account, String password, String publicKey, String pwdQuestion, String pwdAnswer) {
        Message msg = new Message();
        StringBuilder builder = new StringBuilder();
        builder.append(MD5Verify.getMd5(account))
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
        RegisterPresenter presenter = new RegisterPresenter();
        System.out.println(presenter.register("myh", "123", "who i am", "tyf"));
    }

}
