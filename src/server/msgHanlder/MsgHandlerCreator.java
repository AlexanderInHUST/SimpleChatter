package server.msgHanlder;

import static message.MessageConst.FORGET_PSWORD_MSG;
import static message.MessageConst.LOGIN_MSG;
import static message.MessageConst.REGISTER_MSG;

/**
 * Created by tangyifeng on 2017/10/29.
 * Email: yifengtang_hust@outlook.com
 */
public class MsgHandlerCreator {

    private static RegisterMsgHandler registerMsgHandler;
    private static LoginMsgHandler loginMsgHandler;
    private static ForgetPswordHandler forgetPswordHandler;

    private MsgHandlerCreator() {}

    public static MsgHandler create(int kind) {
        switch (kind) {
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
        }
        return null;
    }

}
