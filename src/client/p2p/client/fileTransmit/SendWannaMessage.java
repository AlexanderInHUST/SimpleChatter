package client.p2p.client.fileTransmit;

import message.Message;

import static message.MessageConst.FILE_WANNA_MSG;

/**
 * Created by tangyifeng on 2017/11/3.
 * Email: yifengtang_hust@outlook.com
 */
public class SendWannaMessage {

    public Message getWannaMsg(String fileName) {
        Message msg = new Message();
        msg.setKind(FILE_WANNA_MSG);
        msg.setData(fileName);
        return msg;
    }
}
