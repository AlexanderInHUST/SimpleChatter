package client.account;

import client.c2s.MessageSender;
import message.Message;
import security.MD5Verify;

import static message.MessageConst.ACC_MSG;
import static message.MessageConst.LOGIN_MSG;
import static message.MessageConst.SUCCESS;

/**
 * Created by tangyifeng on 2017/10/31.
 * Email: yifengtang_hust@outlook.com
 */
public class LoginCheck {

    private MessageSender sender;

    public boolean loginCheck(String account, String password) {
        Message msg = getMsg(account, password);
        sender = new MessageSender();
        Message reply = sender.sendMessageUnsafely(msg);
        return reply.getKind() == ACC_MSG && new String(reply.getData()).equals(SUCCESS);
    }

    private Message getMsg(String account, String password) {
        Message msg = new Message();
        StringBuilder builder = new StringBuilder();
        builder.append(account)
                .append(";")
                .append(MD5Verify.getMd5(password));
        msg.setKind(LOGIN_MSG);
        msg.setData(builder.toString());
        return msg;
    }

    public static void main(String[] args) {
        LoginCheck presenter = new LoginCheck();
        System.out.println(presenter.loginCheck("tyf", "123"));
    }
}
