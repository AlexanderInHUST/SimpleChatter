package util;

import static util.Const.DEFAULT_TIMEOUT;

/**
 * Created by tangyifeng on 2017/10/10.
 * Email: yifengtang_hust@outlook.com
 */
public class Timer {

    private long timeout, startTime;
    private boolean startFlag, stopFlag, killFlag;
    private TimerListener timerListener;
    private int state;

    public interface TimerListener {
        void onTimeout();
        void onStop();
        void onKill();
    }

    public Timer() {
        setTimeout(DEFAULT_TIMEOUT);
        startFlag = false;
        stopFlag = false;
        killFlag = false;
        state = 0;
        getCountThread().start();
    }

    public void startCount() {
        startFlag = true;
    }

    public void stopCount() {
        stopFlag = true;
    }

    public void killCount() {
        killFlag = true;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public void setTimerListener(TimerListener timerListener) {
        this.timerListener = timerListener;
    }

    private Thread getCountThread() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                while (!killFlag) {
                    switch (state) {
                        case 0: {
                            if (startFlag) {
                                state = 1;
                            } else {
                                state = 0;
                            }
                            break;
                        }
                        case 1: {
                            startTime = System.currentTimeMillis();
                            state = 2;
                            break;
                        }
                        case 2: {
                            if (System.currentTimeMillis() - startTime <= timeout && !stopFlag) {
                                state = 2;
                            } else if (stopFlag) {
                                state = 4;
                            } else {
                                state = 3;
                            }
                            break;
                        }
                        case 3: {
                            timerListener.onTimeout();
                            return;
                        }
                        case 4: {
                            timerListener.onStop();
                            return;
                        }
                    }
                }
                timerListener.onKill();
            }
        });
    }
//    public static void main(String[] args) {
//        Timer timer = new Timer();
//        timer.setTimerListener(new TimerListener() {
//            @Override
//            public void onTimeout() {
//                System.out.println("good!");
//            }
//
//            @Override
//            public void onStop() {
//                System.out.println("bad!");
//            }
//
//            @Override
//            public void onKill() {
//                System.out.println("NO!");
//            }
//        });
//        timer.startCount();
//        try {
//            Thread.sleep(499);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        timer.stopCount();
//    }

}
