package client.p2p.server.presenter;

import client.p2p.server.msgHandler.IMsgCallback;
import client.p2p.server.msgHandler.MsgHandlerCreator;
import client.presenter.MainDialogPresenter;
import udp.TransmitFile;

import static message.MessageConst.CHAT_SEND_MSG;

/**
 * Created by tangyifeng on 2017/11/4.
 * Email: yifengtang_hust@outlook.com
 */
public class RecvChatPresenter {

    private MainDialogPresenter mainDialogPresenter;

    public RecvChatPresenter(MainDialogPresenter mainDialogPresenter) {
        this.mainDialogPresenter = mainDialogPresenter;
        initialRecvChat();
    }

    private void initialRecvChat() {
        MsgHandlerCreator.setCallback(CHAT_SEND_MSG, new IMsgCallback() {
            @Override
            public Object doSomething(String... data) {
                mainDialogPresenter.addToChatDialog(data[0], data[1]);
                return true;
            }
        });
    }

}
