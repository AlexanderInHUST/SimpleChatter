package udp.prepare;

import message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static message.MessageConst.FILE_READY_MSG;

/**
 * Created by tangyifeng on 2017/11/1.
 * Email: yifengtang_hust@outlook.com
 */
public class ExtraCommunication {

    private String recvHost;
    private int sendPort, recvPort;
    private Socket socket;
    private ObjectOutputStream sendOutputStream, recvOutputStream;
    private ObjectInputStream sendInputStream, recvInputStream;

    public ExtraCommunication(String recvHost, int sendPort, int recvPort) {
        this.recvHost = recvHost;
        this.sendPort = sendPort;
        this.recvPort = recvPort;
    }

    public boolean sendPreparedMsg(String fileName) throws IOException, ClassNotFoundException {
        Message wannaMsg = ExtraCommMsgCreator.getWannaMsg(fileName);
        socket = new Socket(recvHost, recvPort);
        sendOutputStream = new ObjectOutputStream(socket.getOutputStream());
        sendOutputStream.writeObject(wannaMsg);
        sendOutputStream.flush();

        sendInputStream = new ObjectInputStream(socket.getInputStream());
        Message reply = (Message) sendInputStream.readObject();
        if (!(reply.getKind() == FILE_READY_MSG && new String(reply.getData()).equals(fileName))) {
            return false;
        }
        
    }

}
