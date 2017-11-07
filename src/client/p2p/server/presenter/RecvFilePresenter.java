package client.p2p.server.presenter;

import client.p2p.server.msgHandler.MsgHandlerCreator;
import client.presenter.MainDialogPresenter;
import security.MD5Verify;
import udp.TransmitFile;

import static message.MessageConst.FILE_DONE_MSG;
import static message.MessageConst.FILE_WANNA_MSG;

/**
 * Created by tangyifeng on 2017/11/4.
 * Email: yifengtang_hust@outlook.com
 */
public class RecvFilePresenter {

    private String fileName;
    private int recvPort;
    private volatile boolean isDone = false;

    public RecvFilePresenter(int recvPort) {
        this.recvPort = recvPort;
        initialRecvFile();
    }

    private String editFileName(String fileName) {
        return fileName + ".send";
        //todo
    }

    private void initialRecvFile() {
        MsgHandlerCreator.setCallback(FILE_WANNA_MSG, (data) -> {
            fileName = editFileName(data[0]);
            new Thread(new RecvTask()).start();
            return recvPort;
        });

        MsgHandlerCreator.setCallback(FILE_DONE_MSG, (data) -> {
            while (!isDone);
            String md5 = MD5Verify.getFileMD5(fileName);
            return md5 != null && md5.equals(data[0]);
        });
    }

    class RecvTask implements Runnable {

        @Override
        public void run() {
            isDone = false;
            TransmitFile transmitFile = new TransmitFile();
            transmitFile.recv(recvPort, fileName);
            isDone = true;
        }
    }

}
