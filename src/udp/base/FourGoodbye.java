package udp.base;

import util.Log;
import util.SafeTimer;
import udp.util.UDPHelper;


/**
 * Created by tangyifeng on 2017/10/13.
 * Email: yifengtang_hust@outlook.com
 */
public class FourGoodbye {

    private static final String CLASS_NAME = "FourGoodbye";
    private static final boolean IS_DEBUG = false;

    private volatile int state;
    private SafeTimer timer;

    private UDPHelper helper = new UDPHelper();
    private UDPPackageHelper packageHelper = new UDPPackageHelper();

    private UDPPackage ack1, ack2;
    private UDPPackage goodbye1, goodbye2;

    public void startAsSender(String hostname, int sendPort, int recvPort) {
        timer = getTimer();
//        state = 1;
//        while (true) {
//            switch (state) {
//                case 0: {
//                    state = 1;
//                    break;
//                }
//                case 1: {
        goodbye1 = packageHelper.getGoodbyePackage(1).get(0);
        helper.sendUDP(goodbye1, hostname, sendPort);
        Log.log(CLASS_NAME, "goodbye 1 has sent! (send)", IS_DEBUG);
//                    startTimer();
        timer.startCount();
        ack1 = helper.receiveUDP(recvPort);
        Log.log(CLASS_NAME, "ack 1 has received! (send)", IS_DEBUG);
        timer.stopCount();
//                    state = 4;
        timer.killCount();
//        helper.shutdownReceiveUDP();
        Log.log(CLASS_NAME, "See you dude! (send)", IS_DEBUG);
//                    killTimer();
//                    return;
//                    break;
//                }
//                case 2: {
////                    clearTimer();
////                    startTimer();
//                    timer.resetCount();
//                    timer.startCount();
//                    goodbye2 = helper.receiveUDP(recvPort);
//                    Log.log(CLASS_NAME, "goodbye 2 has received! (send)", IS_DEBUG);
//                    timer.stopCount();
//                    state = 3;
//                    break;
//                }
//                case 3: {
////                    clearTimer();
////                    timer.killCount();
//                    timer.resetCount();
//                    ack2 = packageHelper.getAckPackage(2).get(0);
//                    helper.sendUDP(ack2, hostname, sendPort);
//                    Log.log(CLASS_NAME, "ack 2 has sent! (send)", IS_DEBUG);
//                    state = 4;
//                    break;
//                }
//                case 4: {
////                    clearTimer();
//
//                }
//            }
//        }
    }

    public void startAsReceiver(String sendHostname, int sendPort, int recvPort) {
//        while (true) {
//            switch (state) {
//                case 0: {
        ack1 = packageHelper.getAckPackage(1).get(0);
        helper.sendUDP(ack1, sendHostname, sendPort);
        Log.log(CLASS_NAME, "ack 1 has been sent! (recv)", IS_DEBUG);
//                    state = 4;
//                    return;
//                    break;
//                }
//                case 1: {
//                    goodbye2 = packageHelper.getGoodbyePackage(2).get(0);
//                    helper.sendUDP(goodbye2, sendHostname, sendPort);
//                    Log.log(CLASS_NAME, "goodbye 2 has been sent! (recv)", IS_DEBUG);
//                    state = 2;
//                }
//                case 2: {
////                    startTimer();
//                    timer.startCount();
//                    ack2 = helper.receiveUDP(recvPort);
//                    Log.log(CLASS_NAME, "ack 2 has received (recv)!", IS_DEBUG);
//                    timer.stopCount();
//                    state = 4;
//                    break;
//                }
//                case 4: {
////                    clearTimer();
////                    timer.killCount();
////                    helper.shutdownReceiveUDP();
//                    Log.log(CLASS_NAME, "See you dude! (recv)", IS_DEBUG);
////                    killTimer();
//                    return;
//                }
//            }
//        }
    }

    private SafeTimer getTimer() {
        SafeTimer timer = new SafeTimer("FourGoodbye");
        timer.setTimerListener(new SafeTimer.SafeTimerListener() {
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

    public static void main(String[] args) {
        FourGoodbye goodbye1 = new FourGoodbye();
        FourGoodbye goodbye2 = new FourGoodbye();
        new Thread(() -> {
            goodbye1.startAsSender("localhost", 12345, 12346);
        }).start();
        goodbye2.startAsReceiver("localhost", 12346, 12345);
    }
}
