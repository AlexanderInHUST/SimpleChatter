package udp;

import udp.base.StableUDP;
import udp.base.UDPPackage;
import udp.base.UDPPackageHelper;
import util.BitUtil;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by tangyifeng on 2017/10/29.
 * Email: yifengtang_hust@outlook.com
 */
public class TransmitFile {

    private StableUDP stableUDP;
    private UDPPackageHelper packageHelper;

    public TransmitFile() {
        stableUDP = new StableUDP();
        packageHelper = new UDPPackageHelper();
    }

    public boolean send(String sendHost, int sendPort, int recvPort, String fileName, Socket socket) {
        try {
            byte[] fileData = BitUtil.fileToByteArray(fileName);
            ArrayList<UDPPackage> packages = packageHelper.cutDataUDPPackage(fileData);
            stableUDP.setSendData(packages);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean recv(String recvPort, Socket socket) {
        return true;
    }

}
