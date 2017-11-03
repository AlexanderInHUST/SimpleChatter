package client.p2p.server.msgHandler.fileTransmit;

import client.p2p.server.msgHandler.IMsgHandler;
import message.Message;

import java.net.Socket;

import static message.MessageConst.CHECK_FAIL;
import static message.MessageConst.FILE_OK_MSG;
import static message.MessageConst.SUCCESS;

/**
 * Created by tangyifeng on 2017/11/3.
 * Email: yifengtang_hust@outlook.com
 */
public class DoneMsgHandler implements IMsgHandler {
    @Override
    public void refresh() {

    }

    @Override
    public void handleMsg(Message message, Socket socket) {

    }

    public Message getOKMsg(boolean isOK) {
        Message msg = new Message();
        msg.setKind(FILE_OK_MSG);
        msg.setData((isOK) ? SUCCESS : CHECK_FAIL);
        return msg;
    }
}
