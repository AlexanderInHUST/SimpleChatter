package client.p2p.server.msgHandler.fileTransmit;

import client.p2p.server.msgHandler.IMsgHandler;
import message.Message;

import java.net.Socket;

import static message.MessageConst.FILE_READY_MSG;

/**
 * Created by tangyifeng on 2017/11/3.
 * Email: yifengtang_hust@outlook.com
 */
public class WannaMsgHandler implements IMsgHandler {
    @Override
    public void refresh() {

    }

    @Override
    public void handleMsg(Message message, Socket socket) {

    }

    public Message getReadyMsg(String port) {
        Message msg = new Message();
        msg.setKind(FILE_READY_MSG);
        msg.setData(port);
        return msg;
    }
}
