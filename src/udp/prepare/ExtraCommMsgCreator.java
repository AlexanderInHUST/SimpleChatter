package udp.prepare;

import message.Message;

import static message.MessageConst.*;

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

    public static Message getReadyMsg(String port) {
        Message msg = new Message();
        msg.setKind(FILE_READY_MSG);
        msg.setData(port);
        return msg;
    }

    public static Message getDoneMsg(String md5) {
        Message msg = new Message();
        msg.setKind(FILE_DONE_MSG);
        msg.setData(md5);
        return msg;
    }

    public static Message getOKMsg(boolean isOK) {
        Message msg = new Message();
        msg.setKind(FILE_OK_MSG);
        msg.setData((isOK) ? SUCCESS : CHECK_FAIL);
        return msg;
    }

}
