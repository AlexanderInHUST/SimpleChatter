package server.msgHanlder;

import server.msgHanlder.account.ForgetPswordHandler;
import server.msgHanlder.account.LoginMsgHandler;
import server.msgHanlder.account.RegisterMsgHandler;
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
        }
        return null;
    }

}
