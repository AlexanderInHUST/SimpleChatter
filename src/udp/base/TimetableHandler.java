package udp.base;

import util.Log;

import java.util.concurrent.ConcurrentHashMap;

import static udp.UDPConst.SEND_TIMEOUT;

/**
 * Created by tangyifeng on 2017/10/17.
 * Email: yifengtang_hust@outlook.com
 */
public class TimetableHandler {

    private static final String CLASS_NAME = "TimetableHandler";
    private static final boolean IS_DEBUG = true;

    private volatile ConcurrentHashMap<Integer, Long> timeTable;
    private boolean isCorrupt = false;

    public TimetableHandler() {
        timeTable = new ConcurrentHashMap<>();
    }

    public boolean isCorrupt() {
        return isCorrupt;
    }

    public void setCorrupt(boolean corrupt) {
        isCorrupt = corrupt;
    }

    public void remove(int seqNum) {
        synchronized (TimetableHandler.class) {
            if (timeTable.containsKey(seqNum)) {
                timeTable.remove(seqNum);
                Log.log(CLASS_NAME, "time table remove " + seqNum + ". Size is " + timeTable.size(), IS_DEBUG);
            }
        }
    }

    public void refresh(int seqNum) {
        synchronized (TimetableHandler.class) {
            if (timeTable.containsKey(seqNum)) {
                timeTable.replace(seqNum, System.currentTimeMillis());
            }
        }
    }

    public void add(int seqNum) {
        synchronized (TimetableHandler.class) {
            timeTable.put(seqNum, System.currentTimeMillis());
            Log.log(CLASS_NAME, "time table add " + seqNum + ". Size is " + timeTable.size(), IS_DEBUG);
        }
    }

    public boolean isEmpty() {
        synchronized (TimetableHandler.class) {
            return timeTable.isEmpty();
        }
    }

    public boolean contains(int seqNum) {
        synchronized (TimetableHandler.class) {
            return timeTable.containsKey(seqNum);
        }
    }

    public int checkTime() {
        boolean timeoutFound = false;
        int result = -1;
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
        }

        for (int i = 0; i < 10; i = i + 2) {
            handler.remove(i);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
