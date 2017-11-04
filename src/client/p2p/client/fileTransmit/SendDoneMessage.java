package client.p2p.client.fileTransmit;

import client.p2p.client.MessageSender;
import message.Message;

import static message.MessageConst.FILE_DONE_MSG;
import static message.MessageConst.FILE_OK_MSG;
import static message.MessageConst.SUCCESS;

/**
 * Created by tangyifeng on 2017/11/3.
 * Email: yifengtang_hust@outlook.com
 */
public class SendDoneMessage {

    public boolean send(String account, String host, int port, String md5) {
        Message sendMsg = getDoneMsg(account, md5);
        Message reply = MessageSender.sendMessage(sendMsg, host, port);
        return reply != null && reply.getKind() == FILE_OK_MSG && new String(reply.getData()).equals(SUCCESS);
    }

    public Message getDoneMsg(String account, String md5) {
        Message msg = new Message();
        msg.setKind(FILE_DONE_MSG);
        msg.setFromWho(account);
        msg.setData(md5);
        return msg;
    }
}
