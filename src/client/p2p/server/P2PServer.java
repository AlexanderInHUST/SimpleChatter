package client.p2p.server;

import client.p2p.server.msgHandler.MsgHandlerCreator;
import client.p2p.server.presenter.RecvChatPresenter;
import client.p2p.server.presenter.RecvFilePresenter;
import client.presenter.MainDialogPresenter;
import message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by tangyifeng on 2017/11/3.
 * Email: yifengtang_hust@outlook.com
 */
public class P2PServer {

    private MessageQueue queue;
    private volatile boolean isRunning;
    private boolean isKilled;
    private ExecutorService executorService;
    private int serverPort, serverTimeout;

    public P2PServer(int serverPort, int serverTimeout) {
        this.serverPort = serverPort;
        this.serverTimeout = serverTimeout;
        isRunning = false;
        isKilled = false;
        queue = new MessageQueue();
        executorService = Executors.newCachedThreadPool();
        executorService.submit(new ServerReceiveThread());
        queue.start();
        isRunning = true;
    }

    public void close() {
        isRunning = false;
        isKilled = true;
        executorService.shutdown();
        queue.kill();
    }

    public boolean isRunning() {
        return isRunning;
    }

    private class ServerReceiveThread implements Runnable {

        private ServerSocket serverSocket;

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(serverPort);
                serverSocket.setSoTimeout(serverTimeout);
                while (!isRunning);
                while (!isKilled) {
                    Socket socket;
                    try {
                        socket = serverSocket.accept();
                        System.out.println("Msg got!");
                    } catch (SocketTimeoutException e) {
                        continue;
                    }
                    ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                    Message msg = (Message) inputStream.readObject();
                    queue.add(msg, socket); // remember to close it at msghandler!
                }
                serverSocket.close();
                isRunning = false;
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
    }

//    public static void main(String[] args) {
//        P2PServer p2PServer = new P2PServer(25252, 2000);
//        RecvChatPresenter recvChatPresenter = new RecvChatPresenter();
//        RecvFilePresenter recvFilePresenter = new RecvFilePresenter(34333);
//    }
}
