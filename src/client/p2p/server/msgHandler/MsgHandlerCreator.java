package client.p2p.server.msgHandler;

import client.p2p.server.msgHandler.chat.ChatSendHandler;

import static message.MessageConst.CHAT_SEND_MSG;

/**
 * Created by tangyifeng on 2017/11/3.
 * Email: yifengtang_hust@outlook.com
 */
public class MsgHandlerCreator {

    private static ChatSendHandler chatSendHandler;

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
        }
    }
}
