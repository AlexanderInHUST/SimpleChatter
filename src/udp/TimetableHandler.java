package udp;

import java.util.HashMap;

import static util.Const.SEND_TIMEOUT;

/**
 * Created by tangyifeng on 2017/10/17.
 * Email: yifengtang_hust@outlook.com
 */
public class TimetableHandler {

    private volatile HashMap<Integer, Long> timeTable;

    public TimetableHandler() {
        timeTable = new HashMap<>();
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
        synchronized (TimetableHandler.class) {
            boolean timeoutFound = false;
            int result = 0;
            while (!timeoutFound) {
                long curTime = System.currentTimeMillis();
                for (Integer seq : timeTable.keySet()) {
                    if (curTime - timeTable.get(seq) > SEND_TIMEOUT) {
                        timeoutFound = true;
                        result = seq;
                        break;
                    }
                }
            }
            return result;
        }
    }
}
