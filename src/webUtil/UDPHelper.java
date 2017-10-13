package webUtil;

import com.sun.xml.internal.bind.v2.TODO;
import udp.UDPPackage;
import udp.UDPPackageHelper;
import util.Const;
import util.Log;

import java.io.*;
import java.net.*;

import static util.Const.*;

/**
 * Created by tangyifeng on 2017/10/12.
 * Email: yifengtang_hust@outlook.com
 */
public class UDPHelper {

    private static final String CLASS_NAME = "UDPHelper";

    private DatagramPacket sendPacket, receivePacket;
    private DatagramSocket sendSocket, receiveSocket;
    private boolean result;
    private byte[] sendBuff, receiveBuff;
    private String senderHost = "";

    public interface TimeoutListener {
        void onTimeout();
    }

    public boolean sendUDP(UDPPackage pack, String hostname, int port) {
        try {
            sendBuff = new byte[UDP_POWER_BYTE * PACKAGE_LEN + 1];
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ObjectOutputStream outputStream = new ObjectOutputStream(bytes);
            outputStream.writeObject(pack);
            sendSocket = new DatagramSocket(UDP_BACK_PORT);
            InetAddress address = InetAddress.getByName(hostname);
            sendBuff = bytes.toByteArray();
            sendPacket = new DatagramPacket(sendBuff, sendBuff.length, address, port);
            sendSocket.send(sendPacket);

//            Log.log(CLASS_NAME, "pack has been sent!");

            bytes.close();
            outputStream.close();
            sendSocket.close();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
//            Log.log(CLASS_NAME, "pack-sending fail!");
            sendSocket.close();
            result = false;
        }
        return result;
    }

    public UDPPackage receiveUDP(int port){
        try {
            receiveBuff = new byte[UDP_POWER_BYTE * PACKAGE_LEN + 1];
            receiveSocket = new DatagramSocket(port);
            receivePacket = new DatagramPacket(receiveBuff, 0, UDP_POWER_BYTE * PACKAGE_LEN);
            receiveSocket.receive(receivePacket);

//            Log.log(CLASS_NAME, "pack has been received!");

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
//            Log.log(CLASS_NAME, "pack-receive fail");
            return null;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            receiveSocket.close();
//            Log.log(CLASS_NAME, "pack-receive fail");
            return null;
        }
    }

    public void shutdownReceiveUDP() {
        if(!receiveSocket.isClosed()) {
            receiveSocket.close();
        }
//        Log.log(CLASS_NAME, "pack-receive shutdown!");
    }

    public String getSenderHost() {
        return senderHost;
    }

//    public static void main(String[] args) {
//        UDPHelper helper = new UDPHelper();
//        UDPPackageHelper packageHelper = new UDPPackageHelper();
//        new Thread(() -> {
//                UDPPackage pack = helper.receiveUDP(UDP_SEND_PORT);
//                System.out.println(pack.getData());
//        }).start();
//        try {
//            Thread.sleep(400);
//            helper.sendUDP(packageHelper.getAckPackage(1).get(0), "localhost", UDP_SEND_PORT);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
////        helper.shutdownReceiveUDP();
//    }
}
