package client.p2p.client.presenter;

import client.p2p.client.fileTransmit.SendDoneMessage;
import client.p2p.client.fileTransmit.SendWannaMessage;
import security.MD5Verify;
import udp.TransmitFile;

/**
 * Created by tangyifeng on 2017/11/4.
 * Email: yifengtang_hust@outlook.com
 */
public class SendFilePresenter {

    public boolean transmit(String account, String fileName, String hostName, int p2pPort, int recvPort) {
        TransmitFile transmitFile = new TransmitFile();
        transmitFile.readyToSend(fileName);
        String md5 = MD5Verify.getFileMD5(fileName);
        SendWannaMessage sendWannaMessage = new SendWannaMessage();
        int filePort = sendWannaMessage.send(account, hostName, p2pPort, fileName);
        boolean result = transmitFile.send(hostName, filePort, recvPort);
        if (!result) {
            return false;
        }
        SendDoneMessage doneMessage = new SendDoneMessage();
        result = doneMessage.send(account, hostName, p2pPort, md5);
        return result;
    }

    public static void main(String[] args) {
        SendFilePresenter sendFilePresenter = new SendFilePresenter();
        boolean result = sendFilePresenter.transmit("tyf", "/Users/tangyifeng/Desktop/macOS.key", "localhost", 25252, 25253);
        System.out.println(result);
    }

}
