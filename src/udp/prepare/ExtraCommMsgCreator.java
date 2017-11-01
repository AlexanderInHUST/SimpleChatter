package udp.prepare;

import message.Message;

import static message.MessageConst.FILE_DONE_MSG;
import static message.MessageConst.FILE_READY_MSG;
import static message.MessageConst.FILE_WANNA_MSG;

/**
 * Created by tangyifeng on 2017/11/1.
 * Email: yifengtang_hust@outlook.com
 */
public class ExtraCommMsgCreator {

    public static Message getWannaMsg(String fileName) {
        Message msg = new Message();
        msg.setKind(FILE_WANNA_MSG);
        msg.setData(fileName);
        return msg;
    }

    public static Message getReadyMsg(String fileName) {
        Message msg = new Message();
        msg.setKind(FILE_READY_MSG);
        msg.setData(fileName);
        return msg;
    }

    public static Message getDoneMsg() {
        Message msg = new Message();
        msg.setKind(FILE_DONE_MSG);
        msg.setData("");
        return msg;
    }

}
