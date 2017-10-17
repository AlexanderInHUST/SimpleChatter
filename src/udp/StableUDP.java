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
    private volatile HashMap<Integer, Long> timeTable;

    private volatile TreeMap<Integer, UDPPackage> recvData;
    private volatile ArrayList<UDPPackage> sendData;

    private UDPHelper helper = new UDPHelper();
    private UDPPackageHelper packageHelper = new UDPPackageHelper();

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

    public boolean startAsReceiver(int port) {
        receiveHello = new ThreeHello();
        if (!receiveHello.startAsReceiver(port)) {
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
                        UDPPackage pack = helper.receiveUDP(port);
                        if (isRecvGoodbye) {
                            state = 5;
                        } else {
                            if (pack == null) {
                                state = 6;
                            } else {
                                recvTimer.stopCount();
                                recvTimer.killCount();
                                executorService.submit(new RecvThread(pack, port));
                                state = 1;
                            }
                        }
                        break;
                    }
                }
                case 5: {
                    executorService.shutdown();
                    recvGoodbye = new FourGoodbye();
                    recvGoodbye.startAsReceiver(helper.getSenderHost(), port + 1); // care of port!
                    return true;
                }
                case 6: {
                    executorService.shutdown();
                    sendGoodbye = new FourGoodbye();
                    sendGoodbye.startAsSender(helper.getSenderHost(), port + 1); // care of port!
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
                            helper.sendUDP(ackPack, helper.getSenderHost(), port + 1); // care for port!
                        }
                        return;
                    }
                }
            }
        }
    }

    public boolean startAsSender(String hostname, int port) {
        sendHello = new ThreeHello();
        if (!sendHello.startAsSender(hostname, port)) {
            return false;
        }
        sendWindow = new SlidingWindow();
        executorService = Executors.newCachedThreadPool();
        timeTable = new HashMap<>();
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
                            helper.sendUDP(sendData.get(i), hostname, port);
                        }
                        sentNum = sendWindow.getHead() - 1;
                        executorService.submit(new SendTimerThread(hostname, port));
                        state = 2;
                    }
                    break;
                }
                case 2: {
                    if (isCorrupt) {
                        state = 10;
                    } else {
                        UDPPackage someAck = helper.receiveUDP(port + 1); // care of port!
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
                            executorService.submit(new SendThread(someAck, hostname, port + 1)); // care of port!
                            state = 2;
                        }
                    }
                    break;
                }
                case 9: {
                    executorService.shutdown();
                    recvGoodbye = new FourGoodbye();
                    recvGoodbye.startAsReceiver(hostname, port); // care of port!
                    return true;
                }
                case 10: {
                    executorService.shutdown();
                    sendGoodbye = new FourGoodbye();
                    sendGoodbye.startAsSender(hostname, port);
                    return true;
                }
            }
        }
    }

    private class SendThread implements Runnable {

        private int sendState;
        private UDPPackage ack;
        private String hostName;
        private int port;

        private SendThread(UDPPackage ack, String hostName, int port) {
            this.ack = ack;
            this.hostName = hostName;
            this.port = port;
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
                            timeTable.remove(ack.getSeqNum());
                            sendState = 6;
                        }
                        break;
                    }
                    case 6: {
                        if (isCorrupt) {
                            return;
                        } else {
                            if (timeTable.isEmpty()) {
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
                                helper.sendUDP(sendData.get(i), hostName, port);
                                timeTable.put(i, System.currentTimeMillis()); // unsafe
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
        private int port;

        private SendTimerThread(String hostName, int port) {
            sendTimerState = 0;
            countTime = 0;
            seqNum = -1;
            this.hostName = hostName;
            this.port = port;
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
                                seqNum = checkTime();
                                sendTimerState = 1;
                            }
                        }
                        break;
                    }
                    case 1: {
                        if (isCorrupt) {
                            return;
                        } else {
                            helper.sendUDP(sendData.get(seqNum), hostName, port);
                            countTime++;
                            sendTimerState = 0;
                        }
                        break;
                    }
                }
            }
        }

        @SuppressWarnings("unchecked")
        private int checkTime() {
            boolean timeoutFound = false;
            int result = 0;
            while (!timeoutFound) {
                curMap = (HashMap<Integer, Long>) timeTable.clone();
                long curTime = System.currentTimeMillis();
                for (Integer seq : curMap.keySet()) {
                    if (curTime - curMap.get(seq) > SEND_TIMEOUT) {
                        timeoutFound = true;
                        result = seq;
                        break;
                    }
                }
            }
            return result;
        }
    }

    private Timer getRecvTimer() {
        Timer timer = new Timer();
        timer.setTimeout(5000);
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
