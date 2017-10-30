package server;

import message.Message;
import server.msgHanlder.MsgHandler;
import server.msgHanlder.MsgHandlerCreator;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by tangyifeng on 2017/10/29.
 * Email: yifengtang_hust@outlook.com
 */
public class MessageQueue {

    private ConcurrentLinkedQueue<Message> messageQueue;
    private boolean isKilled, isKilledNow;

    public MessageQueue() {
        messageQueue = new ConcurrentLinkedQueue<>();
        isKilled = false;
        isKilledNow = false;
    }

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isKilled && !isKilledNow) {
                    if (!messageQueue.isEmpty()) {
                        Message msg = messageQueue.poll();
                        if (msg == null) {
                            continue;
                        }
                        MsgHandler msgHandler = MsgHandlerCreator.create(msg.getKind());
                        msgHandler.handleMsg(msg.getData());
                    }
                }
            }
        }).start();
    }

    public void add(Message msg) {
        messageQueue.add(msg);
    }

    public void kill() {
        while (!messageQueue.isEmpty() && !isKilledNow);
        this.isKilled = true;
    }

    public void killNow() {
        this.isKilledNow = true;
    }

    public static void main(String[] args) {
        MessageQueue queue = new MessageQueue();
        queue.start();
        for (int i = 0; i < 10; i++) {
            queue.add(new Message(i % 3, "asd"));
        }
        queue.kill();
    }

}
