package util;

import static udp.UDPConst.DEFAULT_TIMEOUT;

/**
 * Created by tangyifeng on 2017/10/10.
 * Email: yifengtang_hust@outlook.com
 */
public class Timer {

    private volatile long timeout, startTime;
    private volatile boolean startFlag, stopFlag, killFlag;
    private TimerListener timerListener;
    private volatile int state;

    private String whoIAm;

    public interface TimerListener {
        void onTimeout();

        void onStop();

        void onKill();
    }

    public Timer(String whoIAm) {
        this.whoIAm = whoIAm;
        setTimeout(DEFAULT_TIMEOUT);
        resetCount();
    }

    public synchronized void startCount() {
        startFlag = true;
        stopFlag = false;
    }

    public synchronized void stopCount() {
        stopFlag = true;
    }

    public synchronized void killCount() {
        killFlag = true;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public synchronized void setTimerListener(TimerListener timerListener) {
        this.timerListener = timerListener;
        getCountThread().start();
    }

    public boolean isStartFlag() {
        return startFlag;
    }

    public void resetCount() {
        startFlag = false;
//        stopFlag = false;
//        killFlag = false;
        state = 0;
    }

    private Thread getCountThread() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                while (!killFlag) {
                    synchronized (this) {
                        switch (state) {
                            case 0: {
                                startTime = System.currentTimeMillis();
                                if (startFlag) {
                                    state = 2;
                                } else {
                                    state = 0;
                                }
                                break;
                            }
//                            case 1: {
//                                state = 2;
//                                break;
//                            }
                            case 2: {
//                            if (System.currentTimeMillis() - startTime <= timeout && !stopFlag) {
//                                state = 2;
//                            } else if (stopFlag) {
//                                state = 4;
//                            } else {
//                                System.out.println(System.currentTimeMillis() + " " + startTime + " " +  (System.currentTimeMillis() - startTime) + " " + timeout);
//                                state = 3;
//                            }
                                if (stopFlag) {
                                    state = 4;
                                } else if (System.currentTimeMillis() - startTime > timeout) {
                                    state = 3;
                                } else {
                                    state = 2;
                                }
                                break;
                            }
                            case 3: {
                                System.out.println(System.currentTimeMillis() + " " + startTime + " " + (System.currentTimeMillis() - startTime) + " " + timeout);
                                timerListener.onTimeout();
                                startFlag = false;
                                state = 0;
                                break;
                            }
                            case 4: {
                                timerListener.onStop();
                                startFlag = false;
                                state = 0;
                                break;
                            }
                        }
                        timerListener.onKill();
                    }

                }
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
//            Thread.sleep(90);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        timer.stopCount();
//    }
}
