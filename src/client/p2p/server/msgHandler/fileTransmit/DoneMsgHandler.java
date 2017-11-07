package client.p2p.server.msgHandler.fileTransmit;

import client.p2p.server.msgHandler.IMsgCallback;
import client.p2p.server.msgHandler.IMsgHandler;
import client.presenter.MainDialogPresenter;
import message.Message;
import udp.TransmitFile;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static message.MessageConst.CHECK_FAIL;
import static message.MessageConst.FILE_OK_MSG;
import static message.MessageConst.SUCCESS;

/**
 * Created by tangyifeng on 2017/11/3.
 * Email: yifengtang_hust@outlook.com
 */
public class DoneMsgHandler implements IMsgHandler {

    private IMsgCallback callback;

    @Override
    public void refresh() {

    }

    @Override
    public void handleMsg(Message message, Socket socket, MainDialogPresenter mainDialogPresenter) {
        String fromWho = message.getFromWho();
        String md5 = new String(message.getData());
        boolean result = (Boolean) callback.doSomething(md5);

        try {
            Message readyMsg = getOKMsg(result);
            readyMsg.setFromWho(fromWho);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(readyMsg);
            outputStream.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Message getOKMsg(boolean isOK) {
        Message msg = new Message();
        msg.setKind(FILE_OK_MSG);
        msg.setData((isOK) ? SUCCESS : CHECK_FAIL);
        return msg;
    }

    @Override
    public void setCallback(IMsgCallback callback) {
        this.callback = callback;
    }
}
