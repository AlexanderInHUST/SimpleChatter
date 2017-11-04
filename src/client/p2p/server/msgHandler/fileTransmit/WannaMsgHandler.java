package client.p2p.server.msgHandler.fileTransmit;

import client.p2p.server.msgHandler.IMsgCallback;
import client.p2p.server.msgHandler.IMsgHandler;
import message.Message;
import udp.TransmitFile;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static message.MessageConst.*;

/**
 * Created by tangyifeng on 2017/11/3.
 * Email: yifengtang_hust@outlook.com
 */
public class WannaMsgHandler implements IMsgHandler {

    private IMsgCallback callback;

    @Override
    public void refresh() {

    }

    @Override
    public void handleMsg(Message message, Socket socket) {
        String fromWho = message.getFromWho();
        String fileName = new String(message.getData());
        int port = (Integer) callback.doSomething(fileName);

        try {
            Message readyMsg = getReadyMsg(Integer.toString(port), port != -1);
            readyMsg.setFromWho(fromWho);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(readyMsg);
            outputStream.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Message getReadyMsg(String port, boolean result) {
        Message msg = new Message();
        msg.setKind(FILE_READY_MSG);
        msg.setData((result) ? port : ERROR);
        return msg;
    }

    @Override
    public void setCallback(IMsgCallback callback) {
        this.callback = callback;
    }
}
