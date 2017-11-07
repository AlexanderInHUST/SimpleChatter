package server.unseen;

import message.Message;
import server.ServerPresenter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by tangyifeng on 2017/10/29.
 * Email: yifengtang_hust@outlook.com
 */
public class MainServerCmd {

    private MessageQueue queue;
    private volatile boolean isRunning;
    private boolean isKilled;
    private ExecutorService executorService;
    private ServerPresenter serverPresenter;

    public MainServerCmd(ServerPresenter serverPresenter) {
        this.serverPresenter = serverPresenter;
        isRunning = false;
        isKilled = false;
        queue = new MessageQueue();
        executorService = Executors.newCachedThreadPool();
    }

    public void start() {
        executorService.submit(new ServerReceiveThread());
        isRunning = true;
        queue.start();
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
                serverSocket = new ServerSocket(ServerConst.SERVER_PORT);
                serverSocket.setSoTimeout(ServerConst.SERVER_TIME_OUT);
                while (!isRunning);
                while (!isKilled) {
                    Socket socket;
                    try {
                        socket = serverSocket.accept();
                    } catch (SocketTimeoutException e) {
                        continue;
                    }
                    ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                    Message msg = (Message) inputStream.readObject();
                    queue.add(msg, socket); // remember to close it at msghandler!
                    serverPresenter.freshMsg("Message from " + msg.getFromWho() + " something about " + msg.getKind());
                }
                serverSocket.close();
                isRunning = false;
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
    }

//    public static void main(String[] args) {
//        MainServerCmd mainServerCmd = new MainServerCmd();
////        try {
////            while (!mainServerCmd.isRunning());
////            Message message = new Message(REGISTER_MSG, "1;1;1;1;1;");
////            Socket socket = new Socket("localhost", SERVER_PORT);
////            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
////            outputStream.writeObject(message);
////            outputStream.flush();
////            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
////            Message msg = (Message) inputStream.readObject();
////            System.out.println(msg.getKind());
////            socket.close();
////            mainServerCmd.close();
////            System.out.println("done!");
////        } catch (IOException | ClassNotFoundException e) {
////            e.printStackTrace();
////        }
//    }

}


