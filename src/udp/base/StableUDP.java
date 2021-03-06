package udp.base;

import util.Log;
import udp.util.UDPHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static udp.UDPConst.*;

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
    private int fileLength;

    private volatile boolean isHelloed = false;
    private volatile boolean isCorrupt = false;
    private volatile boolean isRecvGoodbye = false;
    private volatile boolean isDone = false;
    private volatile boolean isTimeOut = false;
    //    private volatile Timer recvTimer;
    private ExecutorService executorService;
    private ICountListener countListener;

    private volatile int countTime;
    private volatile int sentNum;
    private volatile TimetableHandler timetableHandler;

    private volatile ConcurrentHashMap<Integer, UDPPackage> recvData;
    private volatile ArrayList<UDPPackage> sendData;

    private UDPHelper helper = new UDPHelper();
    private UDPPackageHelper packageHelper = new UDPPackageHelper();

    public ConcurrentLinkedQueue<Integer> recvArray = new ConcurrentLinkedQueue<>();
    public ConcurrentLinkedQueue<Integer> sendArray = new ConcurrentLinkedQueue<>();

    public ArrayList<UDPPackage> getRecvData() {
        ArrayList<UDPPackage> result = new ArrayList<>();
//        System.out.println(recvData.size());
        for (int i = 0; i < recvData.size(); i++) {
            result.add(recvData.get(i));
        }
        return result;
    }

    public void setSendData(ArrayList<UDPPackage> sendData) {
        this.sendData = sendData;
    }

    public boolean isHelloed() {
        return isHelloed;
    }

    public boolean startAsReceiver(int recvPort, int fileLeng) {
        fileLength = fileLeng;
        receiveHello = new ThreeHello(helper);
        if (!receiveHello.startAsReceiver(recvPort)) { // Receiver port
            Log.log(CLASS_NAME, "hello recv failed!", IS_DEBUG);
            return false;
        }
//        recvTimer = getRecvTimer("send");
        isHelloed = true;
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

//                        recvTimer.startCount();
                        UDPPackage pack = helper.receiveUDP(recvPort); // Receiver port
                        Log.log(CLASS_NAME, "pack recved! (main thread in recv)", IS_DEBUG);
//                        recvTimer.stopCount();
//                        recvTimer.resetCount();
                        if (isRecvGoodbye) {
                            state = 5;
                        } else {
                            if (pack == null) {
//                                if (isTimeOut) {
                                state = 6;
//                                } else {
//                                    state = 1;
//                                }
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
//                    recvTimer.killCount();
                    recvGoodbye = new FourGoodbye();
                    Log.log(CLASS_NAME, "goodbye recv! (recv)", IS_DEBUG);
                    recvGoodbye.startAsReceiver(helper.getSenderHost(), helper.getSendPort(), recvPort); // Receiver port
                    return true;
                }
                case 6: {
                    isCorrupt = true;
                    executorService.shutdown();
//                    recvTimer.killCount();
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
        private boolean haveUpdate;

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

                                recvArray.add(someData.getSeqNum());

                                if (someData.isGoodbye()) {
                                    Log.log(CLASS_NAME, "goodbye recv! (subthread in recv)", IS_DEBUG);
                                    helper.shutdownReceiveUDP();
                                    isRecvGoodbye = true;
                                    return;
                                } else if (!receiveWindow.checkWindow(someData.getSeqNum())) {
//                                    synchronized (this) {
//                                        UDPPackage ackPack = packageHelper.getAckPackage(someData.getSeqNum()).get(0);
//                                        Log.log(CLASS_NAME, "ack send with " + someData.getSeqNum() + " ! (fail) (subthread in recv)", IS_DEBUG);
//                                        helper.sendUDP(ackPack, helper.getSenderHost(), port); // care for port!
//                                        Log.log(CLASS_NAME, "window check fail! (subthread in recv)", IS_DEBUG);
//                                        return;
//                                    }
                                    haveUpdate = true;
                                    recvState = 3;
                                } else if (someData.isAck() || someData.isHello()) {
                                    Log.log(CLASS_NAME, "pack ack or hello found! (subthread in recv)", IS_DEBUG);
                                    synchronized (this) {
                                        UDPPackage ack1 = packageHelper.getAckPackage(someData.getSeqNum()).get(0);
                                        helper.sendUDP(ack1, helper.getSenderHost(), port);
                                    }
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
                            synchronized (this) {

                                if (!haveUpdate) {
                                    receiveWindow.updateWindow(someData.getSeqNum());
                                    Log.log(CLASS_NAME, "window update with " + someData.getSeqNum() + " ! (subthread in recv)", IS_DEBUG);
                                }
                                if (!recvData.containsKey(someData.getSeqNum())) {
                                    recvData.put(someData.getSeqNum(), someData);
                                }
//                            if (someData == null) {
//                                System.out.println("shit!");
//                            }
                                UDPPackage ackPack = packageHelper.getAckPackage(someData.getSeqNum()).get(0);
                                Log.log(CLASS_NAME, "ack send with " + someData.getSeqNum() + " ! (subthread in recv)", IS_DEBUG);
                                helper.sendUDP(ackPack, helper.getSenderHost(), port); // care for port!

                                countListener.onCount(someData.getSeqNum(), fileLength);

                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean startAsSender(String hostname, int sendPort, int recvPort, int fileLen) {
        fileLength = fileLen;
        sendHello = new ThreeHello(helper);
        if (!sendHello.startAsSender(hostname, sendPort, recvPort)) {
            Log.log(CLASS_NAME, "hello send failed!", IS_DEBUG);
            return false;
        }
        isHelloed = true;
        Log.log(CLASS_NAME, "hello send succeed!", IS_DEBUG);
        sendWindow = new SlidingWindow();
        executorService = Executors.newCachedThreadPool();
        timetableHandler = new TimetableHandler();
        state = 0;

//        recvTimer = getRecvTimer("recv");
//        recvTimer.setTimeout(RECV_TIMEOUT);

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
//                        recvTimer.startCount();

                        Log.log(CLASS_NAME, "ack ready! (main thread in send)", IS_DEBUG);
                        UDPPackage someAck = helper.receiveUDP(recvPort); // care of port!
                        Log.log(CLASS_NAME, "ack got! (main thread in send)", IS_DEBUG);
//
//                        recvTimer.stopCount();
//                        recvTimer.resetCount();
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
//                    recvTimer.killCount();

                    timetableHandler.setCorrupt(true);
                    executorService.shutdown();
                    recvGoodbye = new FourGoodbye();
                    Log.log(CLASS_NAME, "goodbye recv! (send)", IS_DEBUG);
//                    System.out.println(timetableHandler.isEmpty());
                    recvGoodbye.startAsReceiver(hostname, sendPort, recvPort); // care of port!
                    return true;
                }
                case 10: {
                    isCorrupt = true;
//                    recvTimer.killCount();

                    timetableHandler.setCorrupt(true);
                    executorService.shutdown();
                    sendGoodbye = new FourGoodbye();
                    Log.log(CLASS_NAME, "goodbye send! (send)", IS_DEBUG);
//                    System.out.println(timetableHandler.isEmpty());
                    sendGoodbye.startAsSender(hostname, sendPort, recvPort);
                    return true;
                }
            }
        }
    }

    private class FirstSendThread implements Runnable {

        private int sendSize = Math.min(sendWindow.getHead() / 2 + 1, sendData.size());
        private String hostname;
        private int sendPort;

        private FirstSendThread(String hostname, int sendPort) {
            this.hostname = hostname;
            this.sendPort = sendPort;
        }

        @Override
        public void run() {
            while (!helper.isRunning()) ; // wait!
            synchronized (StableUDP.this) {
                for (int i = 0; i < sendSize; i++) {
                    helper.sendUDP(sendData.get(i), hostname, sendPort); // ?
                    timetableHandler.add(i);

                    sendArray.add(i);
                    countListener.onCount(i, fileLength);

                try {
                    Thread.sleep(SEND_WAIT);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                }
                sentNum = sendSize - 1;
                Log.log(CLASS_NAME, "first data (seq num " + sentNum + ") has been sent!", IS_DEBUG);
                executorService.submit(new SendTimerThread(hostname, sendPort)); // care
            }
        }
    }

    private class SendThread implements Runnable {

        private int sendState;
        private UDPPackage ack;
        private String hostName;
        private int sendPort;
        private Lock lock;

        private SendThread(UDPPackage ack, String hostName, int sendPort) {
            this.ack = ack;
            this.hostName = hostName;
            this.sendPort = sendPort;
            sendState = 4;
            lock = new ReentrantLock();
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
                            Log.log(CLASS_NAME, "window update! " + ack.getSeqNum() + " " + sentNum + " (in sender thread)", IS_DEBUG);
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
                                sendState = 8;
                            }
                        }
                        break;
                    }
//                    case 7: {
//                        if (isCorrupt) {
//                            return;
//                        } else {
////                            lock.lock();
////                            if (!sendWindow.checkWindow(sentNum + 1)) {
////                                Log.log(CLASS_NAME, "sentNum + 1 check fail " + sentNum + " !", IS_DEBUG);
////                                lock.unlock();
////                                return;
////                            } else {
//                                sendState = 8;
////                            }
//                        }
//                        break;
//                    }
                    case 8: {
                        if (isCorrupt) {
//                            lock.unlock();
                            return;
                        } else {
                            synchronized (StableUDP.this) {
                                synchronized (SlidingWindow.class) {
                                    int curHead = sendWindow.getHead();
                                    synchronized (TimetableHandler.class) {
                                        for (int i = sentNum + 1; i <= curHead; i++) {
                                            if (i < sendData.size() && sendWindow.checkWindow(i)) {

                                                sendArray.add(i);

                                                countListener.onCount(i, fileLength);

                                                timetableHandler.add(i);
                                                helper.sendUDP(sendData.get(i), hostName, sendPort);
                                                Log.log(CLASS_NAME, "data (seq num " + i + " ) has been sent!", IS_DEBUG);

                                                try {
                                                    Thread.sleep(SEND_WAIT);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
//                                        }
//                                    timetableHandler.add(i);
                                            }
                                        }
                                    }
                                    sentNum = curHead;
                                    Log.log(CLASS_NAME, "current sent num is " + " " + sentNum, IS_DEBUG);
//                                lock.unlock();
                                }
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
                                if (seqNum == -1) {
                                    return;
                                }
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
                            synchronized (this) {
                                helper.sendUDP(sendData.get(seqNum), hostName, sendPort);
//                                synchronized (TimetableHandler.class) {
                                timetableHandler.refresh(seqNum);
//                                }
                                countTime++;
                                sendTimerState = 0;
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    public void setCountListener(ICountListener countListener) {
        this.countListener = countListener;
    }

//    private Timer getRecvTimer(String who) {
//        Timer timer = new Timer(who);
//        timer.setTimeout(RECV_TIMEOUT);
//        timer.setTimerListener(new Timer.TimerListener() {
//            @Override
//            public void onTimeout() {
//                helper.shutdownReceiveUDP();
//                Log.log(CLASS_NAME, "No recv in a long time!" + who, IS_DEBUG);
//                isTimeOut = true;
////                state = 6;
//            }
//
//            @Override
//            public void onStop() {
//            }
//
//            @Override
//            public void onKill() {
//            }
//        });
//        return timer;
//    }

    public static void main(String[] args) {
        UDPPackageHelper packageHelper = new UDPPackageHelper();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 1000000; i++) {
            builder.append(SAMPLE_TEXT);
        }
        ArrayList<UDPPackage> data = packageHelper.cutDataUDPPackage(builder.toString().getBytes());
        StableUDP sender = new StableUDP();
        sender.setSendData(data);

        long startTime = System.currentTimeMillis();
        System.out.println("start!");

//        for (int i = 1; i < 20; i++) {
        StableUDP recv = new StableUDP();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (recv.startAsReceiver(32323, 12)) {
                    ArrayList<UDPPackage> data = recv.getRecvData();
                    String s = new String(packageHelper.composeDataUDPPackage(data));
//                    System.out.println(s);
                    System.out.println("recv test done!");
//                        for (int i = 0; i < 1440000; i++) {
//                            if (!recv.recvArray.contains(i)) {
//                                System.out.println("shit! " + i);
//                            }
//                            if (!sender.sendArray.contains(i)) {
//                                System.out.println("fuck! " + i);
//                            }
//                        }

                    System.out.println("speed is " + (data.size() * PACKAGE_LEN / 1024) / (double) ((double) (System.currentTimeMillis() - startTime) / 1000) + "kb/s");
                } else {
                    System.out.println("ERROR! hello failed!");
                }
            }
        }).start();

//        System.out.println(data.size());
        sender.startAsSender("localhost", 32323, 32324, 1);
//        do {

//        } while (!recv.isHelloed());
        System.out.println("send test done!");
        System.out.println(data.size());
//        System.out.println(data.size());

//        }
    }
}
