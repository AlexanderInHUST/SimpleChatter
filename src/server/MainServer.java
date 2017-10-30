package server;

import message.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static message.MessageConst.REGISTER_MSG;
import static server.ServerConst.SERVER_BACK_LENGTH;
import static server.ServerConst.SERVER_PORT;
import static server.ServerConst.SERVER_TIME_OUT;

/**
 * Created by tangyifeng on 2017/10/29.
 * Email: yifengtang_hust@outlook.com
 */
public class MainServer {

    private MessageQueue queue;
    private volatile boolean isRunning;
    private boolean isKilled;
    private ExecutorService executorService;

    public MainServer() {
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
                serverSocket = new ServerSocket(SERVER_PORT);
                serverSocket.setSoTimeout(SERVER_TIME_OUT);
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
                }
                serverSocket.close();
                isRunning = false;
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        MainServer mainServer = new MainServer();
//        try {
//            while (!mainServer.isRunning());
//            Message message = new Message(REGISTER_MSG, "1;1;1;1;1;");
//            Socket socket = new Socket("localhost", SERVER_PORT);
//            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
//            outputStream.writeObject(message);
//            outputStream.flush();
//            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
//            Message msg = (Message) inputStream.readObject();
//            System.out.println(msg.getKind());
//            socket.close();
//            mainServer.close();
//            System.out.println("done!");
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
    }

}


