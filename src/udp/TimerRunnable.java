package udp;

import util.Timer;

/**
 * Created by tangyifeng on 2017/10/13.
 * Email: yifengtang_hust@outlook.com
 */
public class TimerRunnable implements Runnable {

    private Timer timer = new Timer();

    @Override
    public void run() {
        timer.startCount();
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }
}
