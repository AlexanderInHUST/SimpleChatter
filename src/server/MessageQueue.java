package server;

import message.Message;
import server.msgHanlder.MsgHandler;
import server.msgHanlder.MsgHandlerCreator;
import server.sql.SqlHelper;

import java.util.concurrent.ConcurrentLinkedQueue;

import static message.MessageConst.REGISTER_MSG;

/**
 * Created by tangyifeng on 2017/10/29.
 * Email: yifengtang_hust@outlook.com
 */
public class MessageQueue {

    private ConcurrentLinkedQueue<Message> messageQueue;
    private boolean isKilled, isKilledNow;
    private SqlHelper sqlHelper;

    public MessageQueue() {
        messageQueue = new ConcurrentLinkedQueue<>();
        sqlHelper = new SqlHelper();
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
                        msgHandler.handleMsg(msg.getData(), sqlHelper);
                    }
                }
                sqlHelper.shutdownSqlHelper();
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
//        for (int i = 0; i < 10; i++) {
//            queue.add(new Message(i % 3, "asd"));
//        }
//        queue.kill();
        queue.add(new Message(REGISTER_MSG, "2;1;1;1;1"));
        queue.add(new Message(REGISTER_MSG, "3;1;1;1;1"));
        queue.add(new Message(REGISTER_MSG, "4;1;1;1;1"));
        queue.kill();
    }

}