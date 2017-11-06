package client.p2p.server;

import client.p2p.server.msgHandler.IMsgHandler;
import client.p2p.server.msgHandler.MsgHandlerCreator;
import message.Message;

import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by tangyifeng on 2017/11/3.
 * Email: yifengtang_hust@outlook.com
 */
public class MessageQueue {

    private ConcurrentLinkedQueue<Message> messageQueue;
    private ConcurrentLinkedQueue<Socket> socketQueue;
    private boolean isKilled, isKilledNow;

    public MessageQueue() {
        messageQueue = new ConcurrentLinkedQueue<>();
        socketQueue = new ConcurrentLinkedQueue<>();
        isKilled = false;
        isKilledNow = false;
    }

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isKilled && !isKilledNow) {
                    if (!messageQueue.isEmpty()) {
                        Message msg;
                        Socket socket;
                        synchronized (MessageQueue.this) {
                            msg = messageQueue.poll();
                            socket = socketQueue.poll();
                        }
                        if (msg == null) {
                            continue;
                        }
                        IMsgHandler msgHandler = MsgHandlerCreator.create(msg.getKind());
                        msgHandler.handleMsg(msg, socket);
                    }
                }
            }
        }).start();
    }

    public synchronized void add(Message msg, Socket socket) {
        messageQueue.add(msg);
        socketQueue.add(socket);
    }

    public void kill() {
        while (!messageQueue.isEmpty() && !isKilledNow);
        this.isKilled = true;
    }

    public void killNow() {
        this.isKilledNow = true;
    }

//    public static void main(String[] args) {
//        MessageQueue queue = new MessageQueue();
//        queue.start();
////        for (int i = 0; i < 10; i++) {
////            queue.add(new Message(i % 3, "asd"));
////        }
////        queue.kill();
//        queue.add(new Message(REGISTER_MSG, "2;1;1;1;1"));
//        queue.add(new Message(REGISTER_MSG, "3;1;1;1;1"));
//        queue.add(new Message(REGISTER_MSG, "4;1;1;1;1"));
//        queue.kill();
//    }

}