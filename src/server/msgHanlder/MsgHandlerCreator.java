package server.msgHanlder;

import server.msgHanlder.account.ForgetPswordHandler;
import server.msgHanlder.account.LoginMsgHandler;
import server.msgHanlder.account.RegisterMsgHandler;
import server.msgHanlder.chat.AskForOfflineMsgHandler;
import server.msgHanlder.chat.CheckStateHandler;
import server.msgHanlder.chat.SendOfflineChatHandler;
import server.msgHanlder.state.LoginHandler;
import server.msgHanlder.state.LogoutHandler;

import static message.MessageConst.*;

/**
 * Created by tangyifeng on 2017/10/29.
 * Email: yifengtang_hust@outlook.com
 */
public class MsgHandlerCreator {

    private static RegisterMsgHandler registerMsgHandler;
    private static LoginMsgHandler loginMsgHandler;
    private static ForgetPswordHandler forgetPswordHandler;

    private static LoginHandler loginHandler;
    private static LogoutHandler logoutHandler;

    private static AskForOfflineMsgHandler askForOfflineMsgHandler;
    private static CheckStateHandler checkStateHandler;
    private static SendOfflineChatHandler sendOfflineChatHandler;

    private MsgHandlerCreator() {}

    public static IMsgHandler create(int kind) {
        switch (kind) {
            // Account
            case REGISTER_MSG: {
                if (registerMsgHandler == null) {
                    registerMsgHandler = new RegisterMsgHandler();
                } else {
                    registerMsgHandler.refresh();
                }
                return registerMsgHandler;
            }
            case LOGIN_MSG: {
                if (loginMsgHandler == null) {
                    loginMsgHandler = new LoginMsgHandler();
                } else {
                    loginMsgHandler.refresh();
                }
                return loginMsgHandler;
            }
            case FORGET_PSWORD_MSG: {
                if (forgetPswordHandler == null) {
                    forgetPswordHandler = new ForgetPswordHandler();
                } else {
                    forgetPswordHandler.refresh();
                }
                return forgetPswordHandler;
            }

            // State
            case LOG_IN_MSG: {
                if (loginHandler == null) {
                    loginHandler = new LoginHandler();
                } else {
                    loginHandler.refresh();
                }
                return loginHandler;
            }
            case LOG_OUT_MSG: {
                if (logoutHandler == null) {
                    logoutHandler = new LogoutHandler();
                } else {
                    logoutHandler.refresh();
                }
                return logoutHandler;
            }

            // Chat
            case CHECK_STATE_MSG: {
                if (checkStateHandler == null) {
                    checkStateHandler = new CheckStateHandler();
                } else {
                    checkStateHandler.refresh();
                }
                return checkStateHandler;
            }
            case ASK_OFFLINE_CHAT_MSG: {
                if (askForOfflineMsgHandler == null) {
                    askForOfflineMsgHandler = new AskForOfflineMsgHandler();
                } else {
                    askForOfflineMsgHandler.refresh();
                }
                return askForOfflineMsgHandler;
            }
            case SEND_OFFLINE_CHAT_MSG: {
                if (sendOfflineChatHandler == null) {
                    sendOfflineChatHandler = new SendOfflineChatHandler();
                } else {
                    sendOfflineChatHandler.refresh();
                }
                return sendOfflineChatHandler;
            }
        }
        return null;
    }

}
