package client.p2p.server.msgHandler;

import client.p2p.server.msgHandler.chat.ChatSendHandler;
import client.p2p.server.msgHandler.fileTransmit.DoneMsgHandler;
import client.p2p.server.msgHandler.fileTransmit.WannaMsgHandler;

import static message.MessageConst.CHAT_SEND_MSG;
import static message.MessageConst.FILE_DONE_MSG;
import static message.MessageConst.FILE_WANNA_MSG;

/**
 * Created by tangyifeng on 2017/11/3.
 * Email: yifengtang_hust@outlook.com
 */
public class MsgHandlerCreator {

    private static ChatSendHandler chatSendHandler;

    private static WannaMsgHandler wannaMsgHandler;
    private static DoneMsgHandler doneMsgHandler;

    public static IMsgHandler create(int kind) {
        switch (kind) {
            case CHAT_SEND_MSG: {
                if (chatSendHandler == null) {
                    chatSendHandler = new ChatSendHandler();
                } else {
                    chatSendHandler.refresh();
                }
                return chatSendHandler;
            }

            case FILE_WANNA_MSG: {
                if (wannaMsgHandler == null) {
                    wannaMsgHandler = new WannaMsgHandler();
                } else {
                    wannaMsgHandler.refresh();
                }
                return wannaMsgHandler;
            }
            case FILE_DONE_MSG: {
                if (doneMsgHandler == null) {
                    doneMsgHandler = new DoneMsgHandler();
                } else {
                    doneMsgHandler.refresh();
                }
                return doneMsgHandler;
            }
        }
        return null;
    }

    public static void setCallback(int kind, IMsgCallback callback) {
        create(kind);
        switch (kind) {
            case CHAT_SEND_MSG: {
                chatSendHandler.setCallback(callback);
                break;
            }
            case FILE_WANNA_MSG: {
                wannaMsgHandler.setCallback(callback);
                break;
            }
            case FILE_DONE_MSG: {
                doneMsgHandler.setCallback(callback);
                break;
            }
        }
    }
}
