package webUtil;

import com.sun.xml.internal.bind.v2.TODO;
import udp.UDPPackage;
import udp.UDPPackageHelper;
import util.Const;
import util.Log;

import java.io.*;
import java.net.*;
import java.util.Random;

import static util.Const.*;

/**
 * Created by tangyifeng on 2017/10/12.
 * Email: yifengtang_hust@outlook.com
 */
public class UDPHelper {

    private static final String CLASS_NAME = "UDPHelper";
    private static final boolean IS_DEBUG = false;

    private DatagramPacket sendPacket, receivePacket;
    private DatagramSocket sendSocket, receiveSocket;
    private boolean result;
    private byte[] sendBuff, receiveBuff;
    private String senderHost = "", receiverHost = "";
    private int sendPort, receiverPort;
    private int offset;

    public interface TimeoutListener {
        void onTimeout();
    }

    public UDPHelper() {
        Random random = new Random();
        offset = random.nextInt() % 500;
    }

    public synchronized boolean sendUDP(UDPPackage pack, String hostname, int port) {
        try {
            sendBuff = new byte[UDP_POWER_BYTE * PACKAGE_LEN + 1];
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ObjectOutputStream outputStream = new ObjectOutputStream(bytes);
            outputStream.writeObject(pack);
            sendSocket = new DatagramSocket(UDP_BACK_PORT - offset);
            InetAddress address = InetAddress.getByName(hostname);
            sendBuff = bytes.toByteArray();
            sendPacket = new DatagramPacket(sendBuff, sendBuff.length, address, port);
            sendSocket.send(sendPacket);

            Log.log(CLASS_NAME, "pack has been sent!", IS_DEBUG);

            bytes.close();
            outputStream.close();
            sendSocket.close();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.log(CLASS_NAME, "pack-sending fail!", IS_DEBUG);
            sendSocket.close();
            result = false;
        }
        return result;
    }

    public synchronized UDPPackage receiveUDP(int port){
        try {
            receiveBuff = new byte[UDP_POWER_BYTE * PACKAGE_LEN + 1];
            receiveSocket = new DatagramSocket(port);
            receivePacket = new DatagramPacket(receiveBuff, 0, UDP_POWER_BYTE * PACKAGE_LEN);
            receiveSocket.receive(receivePacket);

            Log.log(CLASS_NAME, "pack has been received!", IS_DEBUG);

            senderHost = receivePacket.getAddress().getHostAddress();
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
            e.printStackTrace();
            receiveSocket.close();
            Log.log(CLASS_NAME, "pack-receive fail", IS_DEBUG);
            return null;
        }
    }

    public void shutdownReceiveUDP() {
        if(receiveSocket != null && !receiveSocket.isClosed()) {
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

    public static void main(String[] args) {
        UDPHelper helper = new UDPHelper();
        UDPPackageHelper packageHelper = new UDPPackageHelper();
        new Thread(() -> {
                helper.sendUDP(packageHelper.getAckPackage(1).get(0), "localhost", UDP_SEND_PORT);
        }).start();
        UDPPackage pack = helper.receiveUDP(UDP_SEND_PORT);
        helper.shutdownReceiveUDP();
    }
}
