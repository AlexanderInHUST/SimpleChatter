package udp;

import util.BitUtil;
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
    private static final boolean IS_DEBUG = true;

    private volatile int state;
    private int sendCount, receiveCount;

    private Timer timer = getTimer();

    private UDPHelper helper;
    private UDPPackageHelper packageHelper = new UDPPackageHelper();

    private UDPPackage ack1;
    private UDPPackage hello1, hello2;

    public ThreeHello(UDPHelper helper) {
        this.helper = helper;
    }

    public boolean startAsSender(String hostname, int sendPort, int recvPort) {
        while (true) {
            switch (state) {
                case 0: {
                    sendCount = 0;
                    state = 1;
                    break;
                }
                case 1: {
                    hello1 = packageHelper.getHelloPackage(1, recvPort).get(0);
                    helper.sendUDP(hello1, hostname, sendPort);
                    Log.log(CLASS_NAME, "hello 1 has sent!", IS_DEBUG);

//                    startTimer();
                    timer.startCount();
                    ack1 = helper.receiveUDP(recvPort);
                    timer.stopCount();
                    timer.resetCount();
                    Log.log(CLASS_NAME, "ack 1 has received!", IS_DEBUG);

                    if (ack1 != null) {
                        state = 2;
                    } else {
                        state = 4;
                    }
                    break;
                }
                case 2: {
//                    clearTimer();
                    if (packageHelper.checkUDPPackage(ack1) && ack1.isAck() && ack1.getSeqNum() == 1) {
                        Log.log(CLASS_NAME, "ack 1 has accepted!", IS_DEBUG);
                        state = 3;
                        break;
                    }
                    Log.log(CLASS_NAME, "ack 1 has not accepted!", IS_DEBUG);
                    state = 4;
                    break;
                }
                case 3: {
                    UDPPackage hello2 = packageHelper.getHelloPackage(2, recvPort).get(0);
                    helper.sendUDP(hello2, hostname, sendPort);
                    Log.log(CLASS_NAME, "hello 2 has sent!", IS_DEBUG);
//                    killTimer();
                    timer.killCount();
                    return true;
                }
                case 4: {
                    sendCount++;
//                    clearTimer();
                    Log.log(CLASS_NAME, "sender timeout!", IS_DEBUG);
                    if (sendCount > 50) {
                        helper.shutdownReceiveUDP();
//                        killTimer();
                        timer.killCount();
                        return false;
                    } else {
                        state = 1;
                    }
                    break;
                }
            }
        }
    }

    public boolean startAsReceiver(int recvPort) {
        receiveCount = 0;
        while (true) {
            switch (state) {
                case 0: {
//                    timer.setTimeout(5000);
                    timer.startCount();
                    hello1 = helper.receiveUDP(recvPort);
                    timer.stopCount();
                    timer.resetCount();

                    Log.log(CLASS_NAME, "hello 1 has received!", IS_DEBUG);
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
                        Log.log(CLASS_NAME, "hello 1 has accepted!", IS_DEBUG);
                        break;
                    }
                    Log.log(CLASS_NAME, "hello 1 has not accepted!", IS_DEBUG);
                    state = 4;
                    break;
                }
                case 2: {
                    ack1 = packageHelper.getAckPackage(1).get(0);
                    helper.setSendPort(BitUtil.toInt(hello1.getData()));
                    helper.sendUDP(ack1, helper.getSenderHost(), helper.getSendPort());
                    Log.log(CLASS_NAME, "ack 1 has sent!", IS_DEBUG);

//                    startTimer();
                    timer.startCount();
                    hello2 = helper.receiveUDP(recvPort);
                    timer.stopCount();
                    timer.resetCount();

//                    if (hello2 != null) {
                        state = 3;
//                        Log.log(CLASS_NAME, "hello 2 has received!", IS_DEBUG);
//                    } else {
//                        state = 4;
//                    }
                    break;
                }
                case 3: {
//                    clearTimer();
//                    if (packageHelper.checkUDPPackage(hello2) && hello2.isHello() && hello2.getSeqNum() == 2) {
                        Log.log(CLASS_NAME, "hello 2 has accepted!", IS_DEBUG);
//                        killTimer();
                        timer.killCount();
                        return true;
//                    }
//                    Log.log(CLASS_NAME, "hello 2 has not accepted!", IS_DEBUG);
//                    state = 4;
//                    break;
                }
                case 4: {
                    receiveCount++;
                    Log.log(CLASS_NAME, "receiver timeout!", IS_DEBUG);
//                    clearTimer();
                    if (receiveCount > 50) {
                        helper.shutdownReceiveUDP();
//                        killTimer();
                        timer.killCount();
                        return false;
                    } else {
                        state = 0;
                    }
                    break;
                }
            }
        }
    }

    private Timer getTimer() {
        Timer timer = new Timer("ThreeHello");
        timer.setTimerListener(new Timer.TimerListener() {
            @Override
            public void onTimeout() {
                helper.shutdownReceiveUDP();
                state = 4;
            }

            @Override
            public void onStop() {

            }

            @Override
            public void onKill() {

            }
        });
        return timer;
    }

//    private void startTimer() {
//        timer.startCount();
//    }
//
//    private void clearTimer() {
//        timer.stopCount();
//        timer.resetCount();
//    }
//
//    private void killTimer() {
//        timer.killCount();
//    }

    public static void main(String[] args) {
        UDPHelper sendHelper = new UDPHelper();
        UDPHelper recvHelper = new UDPHelper();
        ThreeHello hello1 = new ThreeHello(recvHelper);
        ThreeHello hello2 = new ThreeHello(sendHelper);
        boolean result2;
        new Thread(() -> {
            boolean result1;
            result1 = hello1.startAsReceiver(24242);
            System.out.println(result1 + " recv");
        }).start();
        result2 = hello2.startAsSender("localhost", 24242, 23222);
        System.out.println(result2 + " send");
    }
}