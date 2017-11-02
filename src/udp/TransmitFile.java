package udp;

import security.MD5Verify;
import udp.base.StableUDP;
import udp.base.UDPPackage;
import udp.base.UDPPackageHelper;
import udp.prepare.ExtraCommunication;
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
            String md5 = MD5Verify.getFileMD5(fileName);
            int port = ExtraCommunication.sendPreparedMsg(sendHost, sendPort, fileName);
            if (!stableUDP.startAsSender(sendHost, port, recvPort)) {
                return false;
            }
            return ExtraCommunication.sendDoneMsg(sendHost, sendPort, md5);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean recvStepOne(int recvPort, Socket socket) {
        try {
            ExtraCommunication.replyPrepareMsg(recvPort, socket);
            socket.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean recvStepTwo(boolean isOK, Socket socket) {
        try {
            ExtraCommunication.replyDoneMsg(isOK, socket);
            socket.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
