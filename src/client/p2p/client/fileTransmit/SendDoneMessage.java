package client.p2p.client.fileTransmit;

import message.Message;

import static message.MessageConst.FILE_DONE_MSG;

/**
 * Created by tangyifeng on 2017/11/3.
 * Email: yifengtang_hust@outlook.com
 */
public class SendDoneMessage {

    public Message getDoneMsg(String md5) {
        Message msg = new Message();
        msg.setKind(FILE_DONE_MSG);
        msg.setData(md5);
        return msg;
    }
}
