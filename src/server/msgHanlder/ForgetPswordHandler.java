package server.msgHanlder;

import server.sql.SqlHelper;

import java.sql.Connection;

/**
 * Created by tangyifeng on 2017/10/29.
 * Email: yifengtang_hust@outlook.com
 */
public class ForgetPswordHandler implements MsgHandler {
    @Override
    public void refresh() {

    }

    @Override
    public void handleMsg(String data, SqlHelper sqlHelper) {
        System.out.println("forget");
    }
}
