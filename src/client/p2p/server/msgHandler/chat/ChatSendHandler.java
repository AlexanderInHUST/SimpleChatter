package client.p2p.server.msgHandler.chat;

import client.p2p.server.msgHandler.IMsgHandler;
import message.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static message.MessageConst.ACC_MSG;
import static message.MessageConst.SUCCESS;

/**
 * Created by tangyifeng on 2017/11/3.
 * Email: yifengtang_hust@outlook.com
 */
public class ChatSendHandler implements IMsgHandler {
    @Override
    public void refresh() {

    }

    @Override
    public void handleMsg(Message message, Socket socket) {
        String fromWho = message.getFromWho();
        String data = new String(message.getData());
        //todo
        System.out.println(data);

        try {
            Message okMsg = new Message(ACC_MSG, SUCCESS);
            okMsg.setFromWho(fromWho);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(okMsg);
            outputStream.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
