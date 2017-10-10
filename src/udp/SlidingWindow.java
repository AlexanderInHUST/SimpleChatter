package udp;

import static util.Const.WINDOW_WIDTH;

/**
 * Created by tangyifeng on 2017/10/10.
 * Email: yifengtang_hust@outlook.com
 */
public class SlidingWindow {

    private boolean checkList[];
    private int minPos, maxPos;
    private Thread slideThread;
    private int state;
    private boolean inputFlag;


    public SlidingWindow() {
        minPos = 0;
        maxPos = WINDOW_WIDTH - 1;
        checkList = new boolean[2 * WINDOW_WIDTH];
        for (boolean b : checkList) {
            b = false;
        }
    }




    private boolean check() {
        return true;
    }
}
