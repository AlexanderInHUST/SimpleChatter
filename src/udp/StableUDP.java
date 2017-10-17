package udp;

import util.Timer;
import webUtil.UDPHelper;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static util.Const.RECV_TIMEOUT;
import static util.Const.SEND_COUNT;
import static util.Const.SEND_TIMEOUT;

/**
 * Created by tangyifeng on 2017/10/10.
 * Email: yifengtang_hust@outlook.com
 */
public class StableUDP {

    private SlidingWindow sendWindow, receiveWindow;
    private ThreeHello sendHello, receiveHello;
    private FourGoodbye sendGoodbye, recvGoodbye;
    private int state;
    private volatile boolean isCorrupt = false;
    private volatile boolean isRecvGoodbye = false;
    private volatile boolean isDone = false;
    private volatile Timer recvTimer;
    private ExecutorService executorService;

    private volatile int countTime;
    private volatile int sentNum;
    private TimetableHandler timetableHandler;

    private volatile TreeMap<Integer, UDPPackage> recvData;
    private volatile ArrayList<UDPPackage> sendData;

    private UDPHelper helper = new UDPHelper();
    private UDPPackageHelper packageHelper = new UDPPackageHelper();

    private int sendPort, recvPort;

    public ArrayList<UDPPackage> getRecvData() {
        ArrayList<UDPPackage> result = new ArrayList<>();
        for (int i = 0; i < recvData.size(); i++) {
            result.add(recvData.get(i));
        }
        return result;
    }

    public void setSendData(ArrayList<UDPPackage> sendData) {
        this.sendData = sendData;
    }

    public boolean startAsReceiver(int recvPort) {
        receiveHello = new ThreeHello();
        if (!receiveHello.startAsReceiver(recvPort)) { // Receiver port
            return false;
        }
        receiveWindow = new SlidingWindow();
        recvData = new TreeMap<>();
        executorService = Executors.newCachedThreadPool();
        state = 0;
        while (true) {
            switch (state) {
                case 0: {
                    if (isCorrupt) {
                        state = 6;
                    } else {
                        receiveWindow.initialWindow();
                        state = 1;
                    }
                    break;
                }
                case 1: {
                    if (isCorrupt) {
                        state = 6;
                    } else {
                        recvTimer = getRecvTimer();
                        recvTimer.startCount();
                        UDPPackage pack = helper.receiveUDP(recvPort); // Receiver port
                        if (isRecvGoodbye) {
                            state = 5;
                        } else {
                            if (pack == null) {
                                state = 6;
                            } else {
                                recvTimer.stopCount();
                                recvTimer.killCount();
                                executorService.submit(new RecvThread(pack, helper.getSendPort())); // Sender port
                                state = 1;
                            }
                        }
                        break;
                    }
                }
                case 5: {
                    executorService.shutdown();
                    recvGoodbye = new FourGoodbye();
                    recvGoodbye.startAsReceiver(helper.getSenderHost(), helper.getSendPort(), recvPort); // Receiver port
                    return true;
                }
                case 6: {
                    executorService.shutdown();
                    sendGoodbye = new FourGoodbye();
                    sendGoodbye.startAsSender(helper.getSenderHost(), helper.getSendPort(), recvPort); // care of port! !!!! Something will go wrong
                    return true;
                }
            }
        }
    }

    private class RecvThread implements Runnable {

        private int port;
        private int recvState;
        private UDPPackage someData;

        private RecvThread(UDPPackage someData, int port) {
            recvState = 2;
            this.someData = someData;
            this.port = port;
        }

        @Override
        public void run() {
            while (true) {
                switch (recvState) {
                    case 2: {
                        if (isCorrupt) {
                            return;
                        } else {
                            if (!packageHelper.checkUDPPackage(someData)) {
                                return;
                            } else {
                                if (someData.isGoodbye()) {
                                    helper.shutdownReceiveUDP();
                                    isRecvGoodbye = true;
                                    return;
                                } else if (!sendWindow.checkWindow(someData.getSeqNum())) {
                                    return;
                                } else if (someData.isAck() || someData.isHello()) {
                                    return;
                                } else {
                                    recvState = 3;
                                }
                            }
                        }
                        break;
                    }
                    case 3: {
                        if (isCorrupt) {
                            return;
                        } else {
                            receiveWindow.updateWindow(someData.getSeqNum());
                            recvData.put(someData.getSeqNum(), someData);
                            UDPPackage ackPack = packageHelper.getAckPackage(someData.getSeqNum()).get(0);
                            helper.sendUDP(ackPack, helper.getSenderHost(), port); // care for port!
                        }
                        return;
                    }
                }
            }
        }
    }

    public boolean startAsSender(String hostname, int sendPort, int recvPort) {
        sendHello = new ThreeHello();
        if (!sendHello.startAsSender(hostname, sendPort, recvPort)) {
            return false;
        }
        sendWindow = new SlidingWindow();
        executorService = Executors.newCachedThreadPool();
        timetableHandler = new TimetableHandler();
        state = 0;
        while (true) {
            switch (state) {
                case 0: {
                    if (isCorrupt) {
                        state = 10;
                    } else {
                        sentNum = 0;
                        sendWindow.initialWindow();
                        state = 1;
                    }
                    break;
                }
                case 1: {
                    if (isCorrupt) {
                        state = 10;
                    } else {
                        for (int i = 0; i < sendWindow.getHead(); i++) {
                            helper.sendUDP(sendData.get(i), hostname, sendPort); // ?
                        }
                        sentNum = sendWindow.getHead() - 1;
                        executorService.submit(new SendTimerThread(hostname, sendPort)); // care
                        state = 2;
                    }
                    break;
                }
                case 2: {
                    if (isCorrupt) {
                        state = 10;
                    } else {
                        UDPPackage someAck = helper.receiveUDP(recvPort); // care of port!
                        if (someAck == null) {
                            if (isCorrupt || isDone) {
                                state = 10;
                            } else if (isRecvGoodbye) {
                                state = 9;
                            } else {
                                countTime++;
                                state = 2;
                            }
                        } else {
                            executorService.submit(new SendThread(someAck, hostname, sendPort)); // care of port!
                            state = 2;
                        }
                    }
                    break;
                }
                case 9: {
                    executorService.shutdown();
                    recvGoodbye = new FourGoodbye();
                    recvGoodbye.startAsReceiver(hostname, sendPort, recvPort); // care of port!
                    return true;
                }
                case 10: {
                    executorService.shutdown();
                    sendGoodbye = new FourGoodbye();
                    sendGoodbye.startAsSender(hostname, sendPort, recvPort);
                    return true;
                }
            }
        }
    }

    private class SendThread implements Runnable {

        private int sendState;
        private UDPPackage ack;
        private String hostName;
        private int sendPort;

        private SendThread(UDPPackage ack, String hostName, int sendPort) {
            this.ack = ack;
            this.hostName = hostName;
            this.sendPort = sendPort;
            sendState = 4;
        }

        @Override
        public void run() {
            while (true) {
                switch (sendState) {
                    case 4: {
                        if (isCorrupt) {
                            return;
                        } else {
                            if (!packageHelper.checkUDPPackage(ack)) {
                                return;
                            } else {
                                if (ack.isGoodbye()) {
                                    isRecvGoodbye = true;
                                    helper.shutdownReceiveUDP();
                                    return;
                                } else if (!(ack.isAck() && sendWindow.checkWindow(ack.getSeqNum()))) {
                                    return;
                                } else {
                                    countTime = 0;
                                    sendState = 5;
                                }
                            }
                        }
                        break;
                    }
                    case 5: {
                        if (isCorrupt) {
                            return;
                        } else {
                            sendWindow.updateWindow(ack.getSeqNum());
                            timetableHandler.remove(ack.getSeqNum());
                            sendState = 6;
                        }
                        break;
                    }
                    case 6: {
                        if (isCorrupt) {
                            return;
                        } else {
                            if (timetableHandler.isEmpty()) {
                                isDone = true;
                                helper.shutdownReceiveUDP();
                                return;
                            } else if (sentNum == sendData.size() - 1) {
                                return;
                            } else {
                                sendState = 7;
                            }
                        }
                        break;
                    }
                    case 7: {
                        if (isCorrupt) {
                            return;
                        } else {
                            if (!sendWindow.checkWindow(sentNum + 1)){
                                return;
                            } else {
                                sendState = 8;
                            }
                        }
                        break;
                    }
                    case 8: {
                        if (isCorrupt) {
                            return;
                        } else {
                            for (int i = sentNum + 1; i < sendWindow.getHead(); i++) {
                                helper.sendUDP(sendData.get(i), hostName, sendPort);
                                timetableHandler.add(i);
                            }
                            sentNum = sendWindow.getHead() - 1;
                            return;
                        }
                    }
                }
            }
        }
    }

    private class SendTimerThread implements Runnable {

        private int sendTimerState;
        private int seqNum;
        private HashMap<Integer, Long> curMap;
        private String hostName;
        private int sendPort;

        private SendTimerThread(String hostName, int sendPort) {
            sendTimerState = 0;
            countTime = 0;
            seqNum = -1;
            this.hostName = hostName;
            this.sendPort = sendPort;
        }

        @Override
        public void run() {
            while (true) {
                switch (sendTimerState) {
                    case 0: {
                        if (isCorrupt) {
                            return;
                        } else {
                            if (countTime > SEND_COUNT) {
                                isCorrupt = true;
                                helper.shutdownReceiveUDP();
                                state = 10;
                                return;
                            } else {
                                seqNum = timetableHandler.checkTime();
                                sendTimerState = 1;
                            }
                        }
                        break;
                    }
                    case 1: {
                        if (isCorrupt) {
                            return;
                        } else {
                            helper.sendUDP(sendData.get(seqNum), hostName, sendPort);
                            countTime++;
                            sendTimerState = 0;
                        }
                        break;
                    }
                }
            }
        }
    }

    private Timer getRecvTimer() {
        Timer timer = new Timer();
        timer.setTimeout(RECV_TIMEOUT);
        timer.setTimerListener(new Timer.TimerListener() {
            @Override
            public void onTimeout() {
                helper.shutdownReceiveUDP();
                state = 6;
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
}
