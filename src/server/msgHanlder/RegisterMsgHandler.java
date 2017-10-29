package server.msgHanlder;

import message.Message;

/**
 * Created by tangyifeng on 2017/10/29.
 * Email: yifengtang_hust@outlook.com
 */
public class RegisterMsgHandler implements MsgHandler {
    @Override
    public void refresh() {

    }

    @Override
    public void handleMsg(String data) {
        System.out.println("register");
    }
}
