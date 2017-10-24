package udp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import static util.Const.SEND_TIMEOUT;

/**
 * Created by tangyifeng on 2017/10/17.
 * Email: yifengtang_hust@outlook.com
 */
public class TimetableHandler {

    private volatile HashMap<Integer, Long> timeTable;
    private boolean isCorrupt = false;

    public TimetableHandler() {
        timeTable = new HashMap<>();
    }

    public boolean isCorrupt() {
        return isCorrupt;
    }

    public void setCorrupt(boolean corrupt) {
        isCorrupt = corrupt;
    }

    public void remove(int seqNum) {
        synchronized (TimetableHandler.class) {
            timeTable.remove(seqNum);
        }
    }

    public void add(int seqNum) {
        synchronized (TimetableHandler.class) {
            timeTable.put(seqNum, System.currentTimeMillis());
        }
    }

    public boolean isEmpty() {
        synchronized (TimetableHandler.class) {
            return timeTable.isEmpty();
        }
    }

    public int checkTime() {
        boolean timeoutFound = false;
        int result = 0;
        while (!timeoutFound && !isCorrupt) {
            synchronized (TimetableHandler.class) {
                long curTime = System.currentTimeMillis();
                for (Integer seq : timeTable.keySet()) {
                    if (curTime - timeTable.get(seq) > SEND_TIMEOUT) {
                        timeoutFound = true;
                        result = seq;
                        break;
                    }
                }
            }
        }
        return result;
    }


    public static void main(String[] args) {
        TimetableHandler handler = new TimetableHandler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    int one = handler.checkTime();
                    System.out.println(one);
                    handler.remove(one);
                }
            }
        }).start();
        for (int i = 0; i < 10; i++) {
            handler.add(i);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
