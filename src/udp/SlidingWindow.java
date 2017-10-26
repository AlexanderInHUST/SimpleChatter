package udp;

import util.Log;

import java.util.Random;
import java.util.concurrent.locks.Lock;

import static util.Const.WINDOW_WIDTH;

/**
 * Created by tangyifeng on 2017/10/10.
 * Email: yifengtang_hust@outlook.com
 */
public class SlidingWindow {

    private static final String CLASS_NAME = "SlidingWindow";
    private static final boolean IS_DEBUG = false;

    private volatile boolean checkList[];
    private volatile int head, tail; // head means the last seqnum of available pack
    private volatile int readHead;

    private String who;

    public synchronized int getHead() {
        return readHead;
    }

    public synchronized int getRealHead() {
        return head;
    }

    public synchronized int getTail() {
        return tail;
    }

    public void initialWindow(String who) {
        this.who = who;
        head = WINDOW_WIDTH - 1;
        readHead = head;
        tail = 0;
        checkList = new boolean[4 * WINDOW_WIDTH];
        for (int i = 0; i < checkList.length; i++) {
            checkList[i] = false;
        }
//        Log.log(CLASS_NAME, "window initialed", IS_DEBUG);
    }

    public synchronized boolean checkWindow(int seqNum) {
        boolean result = false;
        Log.log(CLASS_NAME, who + " window checking " + seqNum + " " + checkList[seqNum % checkList.length], IS_DEBUG);
        if (head > tail) {
            if (head >= seqNum % checkList.length && seqNum % checkList.length >= tail &&
                    !checkList[seqNum % checkList.length]) {
                result = true;
            }
        } else {
            if (!(tail > seqNum % checkList.length && seqNum % checkList.length > head) &&
                    !checkList[seqNum % checkList.length]) {
                result = true;
            }
        }
        if (!result) {
            Log.log(CLASS_NAME, who + " window checked " + seqNum + " " + checkList[seqNum % checkList.length], IS_DEBUG);
        }
        return result;
    }

    public synchronized void updateWindow(int seqNum) {
        checkList[seqNum % checkList.length] = true;
        while (checkList[tail]) {
            checkList[tail] = false;
            tail = (tail + 1) % checkList.length;
            head = (head + 1) % checkList.length;
            readHead++;
            checkList[head] = false;
        }
        Log.log(CLASS_NAME, who + " window update " + seqNum + " tail " + tail + " head " + head, IS_DEBUG);
    }

    public static void main(String[] args) {
        SlidingWindow window = new SlidingWindow();
        Random random = new Random();
        window.initialWindow(" ");
        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int seqnum = Math.abs(random.nextInt()) % 6;
//            int seqnum = i;
                    if (window.checkWindow(seqnum)) {
                        window.updateWindow(seqnum);
                        System.out.println(seqnum);
                    } else {
//                        System.out.println("check error " + seqnum);
                    }
                }
            }).start();

        }
    }

}
