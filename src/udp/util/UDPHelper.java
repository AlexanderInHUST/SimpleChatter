package udp.util;

import udp.base.UDPPackage;
import udp.base.UDPPackageHelper;
import util.Log;

import java.io.*;
import java.net.*;
import java.util.Random;

import static udp.UDPConst.*;

/**
 * Created by tangyifeng on 2017/10/12.
 * Email: yifengtang_hust@outlook.com
 */
public class UDPHelper {

    private static final String CLASS_NAME = "UDPHelper";
    private static final boolean IS_DEBUG = false;

    private DatagramSocket sendSocket, receiveSocket;
    private String senderHost = "", receiverHost = "";
    private int sendPort, receiverPort;
    private volatile int offset = 0;
    private byte[] receiveBuff;

    public UDPHelper() {
        receiveBuff = new byte[UDP_POWER_BYTE * PACKAGE_LEN + PACKAGE_LEN];
    }

    public boolean sendUDP(UDPPackage pack, String hostname, int port) {
        byte[] sendBuff;
        DatagramPacket sendPacket;
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ObjectOutputStream outputStream = new ObjectOutputStream(bytes);
            outputStream.writeObject(pack);

//            Random random = new Random();
//            offset = (random.nextInt() * 10000) % 100;

//            if (offset > 10) {
            InetAddress address = InetAddress.getByName(hostname);
            sendBuff = bytes.toByteArray();
            sendPacket = new DatagramPacket(sendBuff, sendBuff.length, address, port);

            synchronized (this) {
                sendSocket = new DatagramSocket(UDP_BACK_PORT + port);
                sendSocket.send(sendPacket);

                Log.log(CLASS_NAME, "pack has been sent!", IS_DEBUG);

                sendSocket.close();
            }
//            }
            bytes.close();
            outputStream.close();
//            sendObjectOutputStream.reset();
//            sendByteOutputStream.reset();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.log(CLASS_NAME, "pack-sending fail!", IS_DEBUG);
            sendSocket.close();
            return false;
        }
    }

    public UDPPackage receiveUDP(int port) {
        try {
            DatagramPacket receivePacket;
            receiveSocket = new DatagramSocket(port);
            receivePacket = new DatagramPacket(receiveBuff, 0, UDP_POWER_BYTE * PACKAGE_LEN + PACKAGE_LEN);
            receiveSocket.setSoTimeout(RECV_TIMEOUT);
            receiveSocket.receive(receivePacket);

            Log.log(CLASS_NAME, "pack has been received!", IS_DEBUG);

            if (senderHost.isEmpty()) {
                senderHost = receivePacket.getAddress().getHostAddress();
            }

            ByteArrayInputStream bytes = new ByteArrayInputStream(receiveBuff);
            ObjectInputStream inputStream = new ObjectInputStream(bytes);
            UDPPackage result = (UDPPackage) inputStream.readObject();

            inputStream.close();
            bytes.close();
            receiveSocket.close();
            return result;
        } catch (SocketException e1) {
            receiveSocket.close();
            Log.log(CLASS_NAME, "pack-receive fail", IS_DEBUG);
            return null;
        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
            receiveSocket.close();
            Log.log(CLASS_NAME, "pack-receive fail", IS_DEBUG);
            return null;
        }
    }

    public void shutdownReceiveUDP() {
        if (receiveSocket != null && !receiveSocket.isClosed()) {
            receiveSocket.close();
        }
        Log.log(CLASS_NAME, "pack-receive shutdown!", IS_DEBUG);
    }

    public String getSenderHost() {
        return senderHost;
    }

    public void setSendPort(int sendPort) {
        this.sendPort = sendPort;
    }

    public int getSendPort() {
        return sendPort;
    }

    public boolean isRunning() {
        return receiveSocket != null && !receiveSocket.isClosed();
    }

//    public static void main(String[] args) {
//        UDPHelper helper = new UDPHelper();
//        UDPPackageHelper packageHelper = new UDPPackageHelper();
//        new Thread(() -> {
//            helper.sendUDP(packageHelper.getAckPackage(1).get(0), "localhost", UDP_SEND_PORT);
//        }).start();
//        UDPPackage pack = helper.receiveUDP(UDP_SEND_PORT);
//        helper.shutdownReceiveUDP();
//    }
}
