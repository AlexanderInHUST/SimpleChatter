package client.p2p.client.presenter;

import client.p2p.client.MessageSender;
import message.Message;

import static message.MessageConst.ACC_MSG;
import static message.MessageConst.CHAT_SEND_MSG;
import static message.MessageConst.SUCCESS;

/**
 * Created by tangyifeng on 2017/11/3.
 * Email: yifengtang_hust@outlook.com
 */
public class SendMessagePresenter {

    public boolean send(String account, String host, int port, String data) {
        Message msg = getMsg(account, data);
        Message reply = MessageSender.sendMessage(msg, host, port);
        return reply != null && reply.getKind() == ACC_MSG && new String(reply.getData()).equals(SUCCESS);
    }

    private Message getMsg(String account, String data) {
        Message message = new Message();
        message.setKind(CHAT_SEND_MSG);
        message.setFromWho(account);
        message.setData(data);
        return message;
    }

    public static void main(String[] args) {
        SendMessagePresenter sendMessagePresenter = new SendMessagePresenter();
        sendMessagePresenter.send("myh","localhost", 25252, "hello!");
    }

}
