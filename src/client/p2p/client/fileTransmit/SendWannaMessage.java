package client.p2p.client.fileTransmit;

import client.p2p.client.MessageSender;
import message.Message;

import static message.MessageConst.FILE_WANNA_MSG;

/**
 * Created by tangyifeng on 2017/11/3.
 * Email: yifengtang_hust@outlook.com
 */
public class SendWannaMessage {

    public int send(String account, String host, int port, String fileName, String fileLength) {
        Message sendMsg = getWannaMsg(account, fileName, fileLength);
        // fileName
        Message reply = MessageSender.sendMessage(sendMsg, host, port);
        // port
        if (reply == null) {
            return -1;
        }
        return Integer.parseInt(new String(reply.getData()));
    }

    public Message getWannaMsg(String account, String fileName, String fileLength) {
        Message msg = new Message();
        msg.setKind(FILE_WANNA_MSG);
        msg.setFromWho(account);
        msg.setData(fileName + ";" + fileLength);
        return msg;
    }
}
