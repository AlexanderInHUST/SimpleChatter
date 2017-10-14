package udp;

import util.Log;
import util.Timer;
import webUtil.UDPHelper;

import java.io.IOException;
import java.util.HashMap;

import static util.Const.RECEIVER;
import static util.Const.SENDER;
import static util.Const.UDP_SEND_PORT;

/**
 * Created by tangyifeng on 2017/10/12.
 * Email: yifengtang_hust@outlook.com
 */
public class ThreeHello {

    // Port should be managed!

    private static final String CLASS_NAME = "ThreeHello";

    private volatile int state;
    private int sendCount, receiveCount;
    private HashMap<Integer, TimerRunnable> timeTable = new HashMap<>();

    private UDPHelper helper = new UDPHelper();
    private UDPPackageHelper packageHelper = new UDPPackageHelper();

    private UDPPackage ack1;
    private UDPPackage hello1, hello2;

    public boolean startAsSender(String hostname, int port) {
        while (true) {
            switch (state) {
                case 0: {
                    sendCount = 0;
                    state = 1;
                    break;
                }
                case 1: {
                    hello1 = packageHelper.getHelloPackage(1).get(0);
                    helper.sendUDP(hello1, hostname, port + 1);
                    Log.log(CLASS_NAME, "hello 1 has sent!");

                    setTimer(1);
                    ack1 = helper.receiveUDP(port);
                    Log.log(CLASS_NAME, "ack 1 has received!");

                    if (ack1 != null) {
                        state = 2;
                    } else {
                        state = 4;
                    }
                    break;
                }
                case 2: {
                    clearTimer(1);
                    if (packageHelper.checkUDPPackage(ack1) && ack1.isAck() && ack1.getSeqNum() == 1) {
                        Log.log(CLASS_NAME, "ack 1 has accepted!");
                        state = 3;
                        break;
                    }
                    Log.log(CLASS_NAME, "ack 1 has not accepted!");
                    state = 4;
                    break;
                }
                case 3: {
                    UDPPackage hello2 = packageHelper.getHelloPackage(2).get(0);
                    helper.sendUDP(hello2, hostname, port + 1);
                    Log.log(CLASS_NAME, "hello 2 has sent!");
                    return true;
                }
                case 4: {
                    sendCount++;
                    clearTimer(1);
                    Log.log(CLASS_NAME, "sender timeout!");
                    if (sendCount > 50) {
                        helper.shutdownReceiveUDP();
                        return false;
                    } else {
                        state = 1;
                    }
                    break;
                }
            }
        }
    }

    public boolean startAsReceiver(int port) {
        while (true) {
            switch (state) {
                case 0: {
                    receiveCount = 0;
                    hello1 = helper.receiveUDP(port + 1);
//                    Log.log(CLASS_NAME, "hello 1 has received!");
                    if (hello1 != null) {
                        state = 1;
                    } else {
                        state = 4;
                    }
                    break;
                }
                case 1: {
                    if (packageHelper.checkUDPPackage(hello1) && hello1.isHello() && hello1.getSeqNum() == 1) {
                        state = 2;
                        Log.log(CLASS_NAME, "hello 1 has accepted!");
                        break;
                    }
                    Log.log(CLASS_NAME, "hello 1 has not accepted!");
                    state = 4;
                    break;
                }
                case 2: {
                    ack1 = packageHelper.getAckPackage(1).get(0);
                    helper.sendUDP(ack1, helper.getSenderHost(), port);
                    Log.log(CLASS_NAME, "ack 1 has sent!");

                    setTimer(1);
                    hello2 = helper.receiveUDP(port + 1);

                    if (hello2 != null) {
                        state = 3;
                        Log.log(CLASS_NAME, "hello 2 has received!");
                    } else {
                        state = 4;
                    }
                    break;
                }
                case 3: {
                    clearTimer(1);
                    if (packageHelper.checkUDPPackage(hello2) && hello2.isHello() && hello2.getSeqNum() == 2) {
                        Log.log(CLASS_NAME, "hello 2 has accepted!");
                        return true;
                    }
                    Log.log(CLASS_NAME, "hello 2 has not accepted!");
                    state = 4;
                    break;
                }
                case 4: {
                    receiveCount++;
                    Log.log(CLASS_NAME, "receiver timeout!");
                    clearTimer(1);
                    if (receiveCount > 50) {
                        helper.shutdownReceiveUDP();
                        return false;
                    } else {
                        state = 0;
                    }
                    break;
                }
            }
        }
    }

    private TimerRunnable getTimer() {
        TimerRunnable hello1Timer = new TimerRunnable();
        Timer timer = hello1Timer.getTimer();
        timer.setTimerListener(new Timer.TimerListener() {
            @Override
            public void onTimeout() {
                helper.shutdownReceiveUDP();
                state = 4;
            }

            @Override
            public void onStop() {}

            @Override
            public void onKill() {}
        });
        return hello1Timer;
    }

    private void setTimer(int seqnum) {
        timeTable.put(seqnum, getTimer());
        timeTable.get(seqnum).run();
    }

    private void clearTimer(int seqnum) {
        if (timeTable.containsKey(seqnum)) {
            timeTable.get(seqnum).getTimer().stopCount();
            timeTable.get(seqnum).getTimer().killCount();
            timeTable.remove(seqnum);
        }
    }

    public static void main(String[] args) {
        ThreeHello hello1 = new ThreeHello();
        ThreeHello hello2 = new ThreeHello();
        boolean result2;
        new Thread(() -> {
            boolean result1;
            result1 = hello1.startAsReceiver(UDP_SEND_PORT);
            System.out.println(result1);
        }).start();
        result2 = hello2.startAsSender("localhost", UDP_SEND_PORT);
        System.out.println(result2);
    }
}