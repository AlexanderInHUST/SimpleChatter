package udp;

import static util.Const.WINDOW_WIDTH;

/**
 * Created by tangyifeng on 2017/10/25.
 * Email: yifengtang_hust@outlook.com
 */
public class ListChecker {

    private volatile boolean checkList[];

    public ListChecker() {
        checkList = new boolean[2 * WINDOW_WIDTH];
        for (int i = 0; i < checkList.length; i++) {
            checkList[i] = false;
        }
    }

    public synchronized boolean get(int pos) {
        return checkList[pos];
    }

    public synchronized void set(int pos, boolean value) {
        checkList[pos] = value;
    }

    public int getLength() {
        return checkList.length;
    }

}
