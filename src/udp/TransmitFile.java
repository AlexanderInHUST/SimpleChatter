package udp;

import security.MD5Verify;
import udp.base.StableUDP;
import udp.base.UDPPackage;
import udp.base.UDPPackageHelper;
import util.BitUtil;

import java.io.*;
import java.net.ServerSocket;
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

    public boolean send(String sendHost, int sendPort, int recvPort, String fileName) {
        try {
            byte[] fileData = BitUtil.fileToByteArray(fileName);
            ArrayList<UDPPackage> packages = packageHelper.cutDataUDPPackage(fileData);
            stableUDP.setSendData(packages);
            return !stableUDP.startAsSender(sendHost, sendPort, recvPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean recv(int recvPort, String fileName) {
        try {
            stableUDP.startAsReceiver(recvPort);
            ArrayList<UDPPackage> packages = stableUDP.getRecvData();
            byte[] fileData = packageHelper.composeDataUDPPackage(packages);
            BitUtil.byteArrayToFile(fileData, fileName);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        TransmitFile transmitFile = new TransmitFile();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                TransmitFile transmitFile1 = new TransmitFile();
                transmitFile1.recv(5656, "/Users/tangyifeng/Desktop/test.mp4");
            }
        }).start();
        transmitFile.send("localhost", 5656, 5858, "/Users/tangyifeng/Desktop/sjhs.ml_0001.mp4");
    }

}
