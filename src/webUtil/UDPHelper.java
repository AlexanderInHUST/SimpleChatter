package webUtil;

import com.sun.xml.internal.bind.v2.TODO;
import udp.UDPPackage;
import util.Const;
import util.Log;

import java.io.*;
import java.net.*;

import static util.Const.PACKAGE_LEN;
import static util.Const.UDP_PORT;
import static util.Const.UDP_POWER_BYTE;

/**
 * Created by tangyifeng on 2017/10/12.
 * Email: yifengtang_hust@outlook.com
 */
public class UDPHelper {

    private static final String CLASS_NAME = "UDPHelper";

    private DatagramPacket packet;
    private DatagramSocket socket;
    private InetAddress address;
    private boolean result;
    private byte[] buff;

    public boolean sendUDP(UDPPackage pack, String hostname) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ObjectOutputStream outputStream = new ObjectOutputStream(bytes);
            outputStream.writeObject(pack);
            socket = new DatagramSocket(UDP_PORT);
            address = InetAddress.getByName("hostname");
            buff = bytes.toByteArray();
            packet = new DatagramPacket(buff, buff.length, address, UDP_PORT);
            socket.send(packet);

            Log.log(CLASS_NAME, "pack has been sent!");

            bytes.close();
            outputStream.close();
            socket.close();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.log(CLASS_NAME, "pack-sending fail!");
            result = false;
        }
        return result;
    }

    public UDPPackage receiveUDP(int port) {
        try {
            socket = new DatagramSocket(UDP_PORT);
            packet = new DatagramPacket(buff, UDP_POWER_BYTE * PACKAGE_LEN);
            socket.receive(packet);

            Log.log(CLASS_NAME, "pack has been received!");

            ByteArrayInputStream bytes = new ByteArrayInputStream(buff);
            ObjectInputStream inputStream = new ObjectInputStream(bytes);
            return (UDPPackage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            Log.log(CLASS_NAME, "pack-receive fail");
            return null;
        }
        // todo: how to stop receiving by timer
    }

}
