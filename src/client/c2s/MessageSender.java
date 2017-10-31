package client.c2s;

import message.Message;
import security.SecurityGuard;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static client.c2s.C2SConst.C2S_SERVER_HOST;
import static client.c2s.C2SConst.C2S_SERVER_PORT;
import static security.SecurityConst.CLIENT_RSA;

/**
 * Created by tangyifeng on 2017/10/30.
 * Email: yifengtang_hust@outlook.com
 */
public class MessageSender {

    private SecurityGuard guard;

    public MessageSender() {};

    public MessageSender(String privateKey) {
        guard = new SecurityGuard(privateKey, CLIENT_RSA);
    }

    public Message sendMessageUnsafely(Message msg) {
        try {
            Socket socket = new Socket(C2S_SERVER_HOST, C2S_SERVER_PORT);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(msg);
            outputStream.flush();
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            Message reply = (Message) inputStream.readObject();
            outputStream.close();
            inputStream.close();

            socket.close();
            return reply;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Message sendMessageSafely(Message msg) {
        Message encryptMsg = new Message();
        encryptMsg.setKind(msg.getKind());
        encryptMsg.setData(guard.encryptByPrivateKey(msg.getData()));
        encryptMsg.setSigns(guard.getSignatures());
        Message reply = sendMessageUnsafely(encryptMsg);
        reply.setData(guard.decryptByPrivateKey(reply.getData()));
        return reply;
    }

}
