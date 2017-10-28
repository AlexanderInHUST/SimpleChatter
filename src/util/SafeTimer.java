package util;

import java.util.*;
import java.util.Timer;

import static util.Const.DEFAULT_TIMEOUT;

/**
 * Created by tangyifeng on 2017/10/28.
 * Email: yifengtang_hust@outlook.com
 */
public class SafeTimer {

    private volatile long timeout, startTime;
    private SafeTimerListener timerListener;
    private java.util.Timer timer;
    private TimeoutTask timeoutTask;

    private String whoIam;

    public interface SafeTimerListener {
        void onTimeout();

        void onStop();

        void onKill();
    }

    public SafeTimer(String whoIam) {
        this.whoIam = whoIam;
        timer = new Timer();
        this.timeout = DEFAULT_TIMEOUT;
    }

    public void startCount() {
        timeoutTask = new TimeoutTask();
        timer.schedule(timeoutTask, 0, timeout);
    }

    public void stopCount() {
        timer.purge();
    }

    public void killCount() {
        timer.cancel();
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public void setTimerListener(SafeTimerListener timerListener) {
        this.timerListener = timerListener;
    }

    private class TimeoutTask extends TimerTask {
        @Override
        public void run() {
            timerListener.onTimeout();
        }
    }

}
