package udp;

import util.Log;
import util.Timer;
import webUtil.UDPHelper;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static util.Const.*;

/**
 * Created by tangyifeng on 2017/10/10.
 * Email: yifengtang_hust@outlook.com
 */
public class StableUDP {

    private static final String CLASS_NAME = "StableUDP";
    private static final boolean IS_DEBUG = true;

    private SlidingWindow sendWindow, receiveWindow;
    private ThreeHello sendHello, receiveHello;
    private FourGoodbye sendGoodbye, recvGoodbye;
    private int state;
    private volatile boolean isCorrupt = false;
    private volatile boolean isRecvGoodbye = false;
    private volatile boolean isDone = false;
    private volatile boolean isTimeOut = false;
    private volatile Timer recvTimer;
    private ExecutorService executorService;

    private volatile int countTime;
    private volatile int sentNum;
    private volatile TimetableHandler timetableHandler;

    private volatile ConcurrentHashMap<Integer, UDPPackage> recvData;
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

    public boolean startAsReceiver(int recvPort) {
        receiveHello = new ThreeHello(helper);
        recvTimer = getRecvTimer("send");
        if (!receiveHello.startAsReceiver(recvPort)) { // Receiver port
            Log.log(CLASS_NAME, "hello recv failed!", IS_DEBUG);
            return false;
        }
        Log.log(CLASS_NAME, "hello recv succeed!", IS_DEBUG);
        receiveWindow = new SlidingWindow();
        recvData = new ConcurrentHashMap<>();
        executorService = Executors.newCachedThreadPool();
        state = 0;
        while (true) {
            switch (state) {
                case 0: {
                    if (isCorrupt) {
                        state = 6;
                    } else {
                        receiveWindow.initialWindow("recv");
                        Log.log(CLASS_NAME, "window recv initialed!", IS_DEBUG);
                        state = 1;
                    }
                    break;
                }
                case 1: {
                    if (isCorrupt) {
                        state = 6;
                    } else {
                        recvTimer.startCount();
                        UDPPackage pack = helper.receiveUDP(recvPort); // Receiver port
                        Log.log(CLASS_NAME, "pack recved! (main thread in recv)", IS_DEBUG);
                        recvTimer.stopCount();
                        recvTimer.resetCount();
                        if (isRecvGoodbye) {
                            state = 5;
                        } else {
                            if (pack == null) {
                                if (isTimeOut) {
                                    state = 6;
                                } else {
                                    state = 1;
                                }
                            } else {
                                executorService.submit(new RecvThread(pack, helper.getSendPort())); // Sender port
                                Log.log(CLASS_NAME, "recv thread submitted!", IS_DEBUG);
                                state = 1;
                            }
                        }

                        break;
                    }
                }
                case 5: {
                    isCorrupt = true;
                    executorService.shutdown();
                    recvTimer.killCount();
                    recvGoodbye = new FourGoodbye();
                    Log.log(CLASS_NAME, "goodbye recv! (recv)", IS_DEBUG);
                    recvGoodbye.startAsReceiver(helper.getSenderHost(), helper.getSendPort(), recvPort); // Receiver port
                    return true;
                }
                case 6: {
                    isCorrupt = true;
                    executorService.shutdown();
                    recvTimer.killCount();
                    sendGoodbye = new FourGoodbye();
                    Log.log(CLASS_NAME, "goodbye send! (recv)", IS_DEBUG);
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
                                Log.log(CLASS_NAME, "pack check fail! (subthread in recv)", IS_DEBUG);
                                return;
                            } else {
                                if (someData.isGoodbye()) {
                                    Log.log(CLASS_NAME, "goodbye recv! (subthread in recv)", IS_DEBUG);
                                    helper.shutdownReceiveUDP();
                                    isRecvGoodbye = true;
                                    return;
                                } else if (!receiveWindow.checkWindow(someData.getSeqNum())) {
                                    UDPPackage ackPack = packageHelper.getAckPackage(someData.getSeqNum()).get(0);
                                    Log.log(CLASS_NAME, "ack send with " + someData.getSeqNum() + " ! (fail) (subthread in recv)", IS_DEBUG);
                                    helper.sendUDP(ackPack, helper.getSenderHost(), port); // care for port!
                                    Log.log(CLASS_NAME, "window check fail! (subthread in recv)", IS_DEBUG);
                                    return;
                                } else if (someData.isAck() || someData.isHello()) {
                                    Log.log(CLASS_NAME, "pack ack or hello found! (subthread in recv)", IS_DEBUG);
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
                            Log.log(CLASS_NAME, "window update with " + someData.getSeqNum() + " ! (subthread in recv)", IS_DEBUG);
                            recvData.put(someData.getSeqNum(), someData);
                            UDPPackage ackPack = packageHelper.getAckPackage(someData.getSeqNum()).get(0);
                            Log.log(CLASS_NAME, "ack send with " + someData.getSeqNum() + " ! (subthread in recv)", IS_DEBUG);
                            helper.sendUDP(ackPack, helper.getSenderHost(), port); // care for port!
                        }
                        return;
                    }
                }
            }
        }
    }

    public boolean startAsSender(String hostname, int sendPort, int recvPort) {
        sendHello = new ThreeHello(helper);
        if (!sendHello.startAsSender(hostname, sendPort, recvPort)) {
            Log.log(CLASS_NAME, "hello send failed!", IS_DEBUG);
            return false;
        }
        Log.log(CLASS_NAME, "hello send succeed!", IS_DEBUG);
        sendWindow = new SlidingWindow();
        executorService = Executors.newCachedThreadPool();
        timetableHandler = new TimetableHandler();
        state = 0;

        recvTimer = getRecvTimer("recv");

//        recvTimer.setTimeout(5000);

//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        while (true) {
            switch (state) {
                case 0: {
                    if (isCorrupt) {
                        state = 10;
                    } else {
                        sentNum = 0;
                        sendWindow.initialWindow("send ");
                        Log.log(CLASS_NAME, "window send initialed!", IS_DEBUG);
                        state = 1;
                    }
                    break;
                }
                case 1: {
                    if (isCorrupt) {
                        state = 10;
                    } else {
                        executorService.submit(new FirstSendThread(hostname, sendPort));
                        state = 2;
                    }
                    break;
                }
                case 2: {
                    if (isCorrupt) {
                        state = 10;
                    } else {
                        recvTimer.startCount();

                        Log.log(CLASS_NAME, "ack ready! (main thread in send)", IS_DEBUG);
                        UDPPackage someAck = helper.receiveUDP(recvPort); // care of port!
                        Log.log(CLASS_NAME, "ack got! (main thread in send)", IS_DEBUG);
//
                       recvTimer.stopCount();
                        recvTimer.resetCount();
                        if (someAck == null) {
                            if (isCorrupt || isDone) {
                                state = 10;
                            } else if (isRecvGoodbye) {
                                state = 9;
                            } else {
//                                countTime++;
//                                state = 2;
                                state = 10;
                            }
                        } else {
                            executorService.submit(new SendThread(someAck, hostname, sendPort)); // care of port!
                            state = 2;
                        }
                    }
                    break;
                }
                case 9: {
                    isCorrupt = true;
                    recvTimer.killCount();

                    timetableHandler.setCorrupt(true);
                    executorService.shutdown();
                    recvGoodbye = new FourGoodbye();
                    Log.log(CLASS_NAME, "goodbye recv! (send)", IS_DEBUG);
                    System.out.println(timetableHandler.isEmpty());
                    recvGoodbye.startAsReceiver(hostname, sendPort, recvPort); // care of port!
                    return true;
                }
                case 10: {
                    isCorrupt = true;
                    recvTimer.killCount();

                    timetableHandler.setCorrupt(true);
                    executorService.shutdown();
                    sendGoodbye = new FourGoodbye();
                    Log.log(CLASS_NAME, "goodbye send! (send)", IS_DEBUG);
                    System.out.println(timetableHandler.isEmpty());
                    sendGoodbye.startAsSender(hostname, sendPort, recvPort);
                    return true;
                }
            }
        }
    }

    private class FirstSendThread implements Runnable {

        private int sendSize = Math.min(sendWindow.getHead() + 1, sendData.size());
        private String hostname;
        private int sendPort;

        private FirstSendThread(String hostname, int sendPort) {
            this.hostname = hostname;
            this.sendPort = sendPort;
        }

        @Override
        public void run() {
            while (!helper.isRunning()) ; // wait!
            for (int i = 0; i < sendSize; i++) {
                helper.sendUDP(sendData.get(i), hostname, sendPort); // ?
                timetableHandler.add(i);
            }
            sentNum = sendSize - 1;
            Log.log(CLASS_NAME, "first data (seq num " + sentNum + ") has been sent!", IS_DEBUG);
            executorService.submit(new SendTimerThread(hostname, sendPort)); // care
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
                                Log.log(CLASS_NAME, "ack check failed! (in sender thread)", IS_DEBUG);
                                return;
                            } else {
                                if (ack.isGoodbye()) {
                                    isRecvGoodbye = true;
                                    helper.shutdownReceiveUDP();
                                    return;
                                } else if (!(ack.isAck() && sendWindow.checkWindow(ack.getSeqNum()))) {
                                    Log.log(CLASS_NAME, "ack window check fail! " + sendWindow.getTail() + " " + sendWindow.getRealHead() + "  (in sender thread)", IS_DEBUG);
                                    return;
                                } else {
                                    timetableHandler.remove(ack.getSeqNum());
                                    Log.log(CLASS_NAME, "ack accepted! " + ack.getSeqNum() + " (in sender thread)", IS_DEBUG);
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
                            Log.log(CLASS_NAME, "window update! " + ack.getSeqNum() + " (in sender thread)", IS_DEBUG);
                            sendState = 6;
                        }
                        break;
                    }
                    case 6: {
                        if (isCorrupt) {
                            return;
                        } else {
//                            if (timetableHandler.isEmpty()) {
//                                isDone = true;
//                                helper.shutdownReceiveUDP();
//                                return;
//                            } else if (sentNum == sendData.size() - 1) {
//                                return;
//                            } else {
//                                sendState = 7;
//                            }
                            if (timetableHandler.isEmpty() && sentNum == sendData.size() - 1 && sendWindow.getHead() == sendData.size() - 1 + WINDOW_WIDTH) {
//                            if (sendWindow.getHead() == sendData.size() - 1 + WINDOW_WIDTH) {
                                Log.log(CLASS_NAME, "all sent !(send thread)", IS_DEBUG);
                                isDone = true;
                                helper.shutdownReceiveUDP();
                                return;
//                            } else if (sentNum == sendData.size() - 1) {
//                                return;
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
                            if (!sendWindow.checkWindow(sentNum + 1)) {
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
                            synchronized (SendTimerThread.class) {
                                for (int i = sentNum + 1; i <= sendWindow.getHead(); i++) {
                                    if (i < sendData.size()) {
                                        timetableHandler.add(i);
                                        helper.sendUDP(sendData.get(i), hostName, sendPort);
                                        Log.log(CLASS_NAME, "data (seq num " + i + " ) has been sent!", IS_DEBUG);
//                                    timetableHandler.add(i);
                                    }
                                }
                                sentNum = sendWindow.getHead();
                                return;
                            }
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
                                Log.log(CLASS_NAME, "found seqnum " + seqNum + " timeout! (Send Timer Thread)", IS_DEBUG);
//                                timetableHandler.remove(seqNum);
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
                            timetableHandler.refresh(seqNum);
                            countTime++;
                            sendTimerState = 0;
                        }
                        break;
                    }
                }
            }
        }
    }

    private Timer getRecvTimer(String who) {
        Timer timer = new Timer(who);
        timer.setTimeout(RECV_TIMEOUT);
        timer.setTimerListener(new Timer.TimerListener() {
            @Override
            public void onTimeout() {
                helper.shutdownReceiveUDP();
                Log.log(CLASS_NAME, "No recv in a long time!" + who, IS_DEBUG);
                isTimeOut = true;
//                state = 6;
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
        UDPPackageHelper packageHelper = new UDPPackageHelper();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            builder.append(SAMPLE_TEXT);
        }
        StableUDP sender = new StableUDP();
        StableUDP recv = new StableUDP();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (recv.startAsReceiver(32323)) {
                    ArrayList<UDPPackage> data = recv.getRecvData();
                    String s = new String(packageHelper.composeDataUDPPackage(data));
                    System.out.println(s);
                    System.out.println("recv test done!");
                }
            }
        }).start();
        ArrayList<UDPPackage> data = packageHelper.cutDataUDPPackage(builder.toString().getBytes());
        System.out.println(data.size());
        sender.setSendData(data);
        sender.startAsSender("localhost", 32323, 32324);
        System.out.println("send test done!");
    }
}
