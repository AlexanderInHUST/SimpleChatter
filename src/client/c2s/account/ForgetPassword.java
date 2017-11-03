package client.c2s.account;

import client.c2s.MessageSender;
import message.Message;
import security.MD5Verify;

import static message.MessageConst.FORGET_PSWORD_MSG;

/**
 * Created by tangyifeng on 2017/10/31.
 * Email: yifengtang_hust@outlook.com
 */
public class ForgetPassword {

    private MessageSender sender;

    public String forgetPassword(String account, String pwdQuestion, String pwdAnswer, String newPassword) {
        Message msg = getMsg(account, pwdQuestion, pwdAnswer, newPassword);
        sender = new MessageSender();
        Message reply = sender.sendMessageUnsafely(msg);
        return new String(reply.getData());
    }

    private Message getMsg(String account, String pwdQuestion, String pwdAnswer, String newPassword) {
        Message msg = new Message();
        StringBuilder builder = new StringBuilder();
        builder.append(account)
                .append(";")
                .append(MD5Verify.getMd5(pwdQuestion))
                .append(";")
                .append(MD5Verify.getMd5(pwdAnswer))
                .append(";")
                .append(MD5Verify.getMd5(newPassword));
        msg.setKind(FORGET_PSWORD_MSG);
        msg.setData(builder.toString());
        return msg;
    }

    public static void main(String[] args) {
        ForgetPassword forgetPassword = new ForgetPassword();
        String result = forgetPassword.forgetPassword("myh", "who u ar", "tyf", "1234");
        System.out.println(result);
    }
}
