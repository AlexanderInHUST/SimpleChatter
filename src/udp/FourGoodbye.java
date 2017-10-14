package udp;

import util.Log;
import util.Timer;
import webUtil.UDPHelper;

import java.util.HashMap;

import static util.Const.UDP_SEND_PORT;

/**
 * Created by tangyifeng on 2017/10/13.
 * Email: yifengtang_hust@outlook.com
 */
public class FourGoodbye {

    private static final String CLASS_NAME = "FourGoodbye";
    private static final boolean IS_DEBUG = true;

    private volatile int state;
    private HashMap<Integer, TimerRunnable> timeTable = new HashMap<>();

    private UDPHelper helper = new UDPHelper();
    private UDPPackageHelper packageHelper = new UDPPackageHelper();

    private UDPPackage ack1, ack2;
    private UDPPackage goodbye1, goodbye2;

    private void startAsSender(String hostname, int port) {
        while (true) {
            switch (state) {
                case 0: {
                    state = 1;
                    break;
                }
                case 1: {
                    goodbye1 = packageHelper.getGoodbyePackage(1).get(0);
                    helper.sendUDP(goodbye1, hostname, port + 1);
                    Log.log(CLASS_NAME, "goodbye 1 has sent!", IS_DEBUG);

                    setTimer(1);
                    ack1 = helper.receiveUDP(port);
                    Log.log(CLASS_NAME, "ack 1 has received!", IS_DEBUG);
                    state = 2;
                    break;
                }
                case 2: {
                    clearTimer(1);
                    setTimer(1);
                    goodbye2 = helper.receiveUDP(port);
                    Log.log(CLASS_NAME, "goodbye 2 has received!", IS_DEBUG);
                    state = 3;
                    break;
                }
                case 3: {
                    clearTimer(1);
                    ack2 = packageHelper.getAckPackage(2).get(0);
                    helper.sendUDP(ack2, hostname, port + 1);
                    Log.log(CLASS_NAME, "ack 2 has received!", IS_DEBUG);
                    state = 4;
                    break;
                }
                case 4: {
                    clearTimer(1);
                    helper.shutdownReceiveUDP();
                    Log.log(CLASS_NAME, "See you dude!", IS_DEBUG);
                    return;
                }
            }
        }
    }

    public void startAsReceiver(String sendHostname, int port) {
        while (true) {
            switch (state) {
                case 0: {
                    ack1 = packageHelper.getAckPackage(1).get(0);
                    helper.sendUDP(ack1, sendHostname, port);
                    Log.log(CLASS_NAME, "ack 1 has been sent!", IS_DEBUG);
                    state = 1;
                    break;
                }
                case 1: {
                    goodbye2 = packageHelper.getGoodbyePackage(2).get(0);
                    helper.sendUDP(goodbye2, sendHostname, port);
                    Log.log(CLASS_NAME, "goodbye 2 has been sent!", IS_DEBUG);
                    state = 2;
                }
                case 2: {
                    setTimer(1);
                    ack2 = helper.receiveUDP(port + 1);
                    Log.log(CLASS_NAME, "ack 2 has received!", IS_DEBUG);
                    state = 4;
                    break;
                }
                case 4: {
                    clearTimer(1);
                    helper.shutdownReceiveUDP();
                    Log.log(CLASS_NAME, "See you dude!", IS_DEBUG);
                    return;
                }
            }
        }
    }

    private TimerRunnable getTimer1() {
        TimerRunnable goodbye1Timer = new TimerRunnable();
        Timer timer = goodbye1Timer.getTimer();
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
        return goodbye1Timer;
    }

    private void setTimer(int seqnum) {
        timeTable.put(seqnum, getTimer1());
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
        FourGoodbye goodbye1 = new FourGoodbye();
        FourGoodbye goodbye2 = new FourGoodbye();
        new Thread(() -> {
            goodbye1.startAsReceiver("localhost", UDP_SEND_PORT);
        }).start();
        goodbye2.startAsSender("localhost", UDP_SEND_PORT);
    }
}
