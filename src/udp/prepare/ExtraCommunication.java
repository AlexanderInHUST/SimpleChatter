package udp.prepare;

import message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static message.MessageConst.FILE_OK_MSG;
import static message.MessageConst.FILE_READY_MSG;
import static message.MessageConst.SUCCESS;

/**
 * Created by tangyifeng on 2017/11/1.
 * Email: yifengtang_hust@outlook.com
 */
public class ExtraCommunication {

    // for sender
    public static int sendPreparedMsg(String sendHost, int sendPort, String fileName) throws IOException, ClassNotFoundException {
        Message wannaMsg = ExtraCommMsgCreator.getWannaMsg(fileName);
        Socket socket = new Socket(sendHost, sendPort);
        ObjectOutputStream sendOutputStream = new ObjectOutputStream(socket.getOutputStream());
        sendOutputStream.writeObject(wannaMsg);
        sendOutputStream.flush();

        ObjectInputStream sendInputStream = new ObjectInputStream(socket.getInputStream());
        Message reply = (Message) sendInputStream.readObject();
        if (reply.getKind() != FILE_READY_MSG) {
            return -1;
        }

        sendInputStream.close();
        sendOutputStream.close();
        socket.close();
        return Integer.parseInt(new String(reply.getData()));
    }

    // for recv in Msg queue
    public static boolean replyPrepareMsg(int port, Socket socket) throws IOException {
//        this.socket = socket;
        Message readyMsg = ExtraCommMsgCreator.getReadyMsg(Integer.toString(port));
        ObjectOutputStream recvOutputStream = new ObjectOutputStream(socket.getOutputStream());
        recvOutputStream.writeObject(readyMsg);
        recvOutputStream.flush();

        recvOutputStream.close();
        return true;
    }

    // for send
    public static boolean sendDoneMsg(String sendHost, int sendPort, String md5) throws IOException, ClassNotFoundException {
        Message doneMsg = ExtraCommMsgCreator.getDoneMsg(md5);
        Socket socket = new Socket(sendHost, sendPort);
        ObjectOutputStream recvOutputStream = new ObjectOutputStream(socket.getOutputStream());
        recvOutputStream.writeObject(doneMsg);
        recvOutputStream.flush();

        ObjectInputStream recvInputStream = new ObjectInputStream(socket.getInputStream());
        Message reply = (Message) recvInputStream.readObject();
        recvInputStream.close();
        recvOutputStream.close();
        socket.close();
        return reply.getKind() == FILE_OK_MSG && new String(reply.getData()).equals(SUCCESS);
    }

    // for recv in Msg queue
    public static boolean replyDoneMsg(boolean isOK, Socket socket) throws IOException {
        Message okMsg = ExtraCommMsgCreator.getOKMsg(isOK);
        ObjectOutputStream recvOutputStream = new ObjectOutputStream(socket.getOutputStream());
        recvOutputStream.writeObject(okMsg);
        recvOutputStream.flush();

        recvOutputStream.close();
        return true;
    }

}
