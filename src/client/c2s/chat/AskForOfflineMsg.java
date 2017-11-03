package client.c2s.chat;

import client.c2s.MessageSender;
import client.sql.SqlHelper;
import client.sql.detail.SqlSecurity;
import message.Message;
import message.MessageCoder;

import java.util.ArrayList;
import java.util.HashMap;

import static message.MessageConst.ASK_OFFLINE_CHAT_MSG;
import static message.MessageConst.CHECK_FAIL;

/**
 * Created by tangyifeng on 2017/11/3.
 * Email: yifengtang_hust@outlook.com
 */
public class AskForOfflineMsg {

    private MessageSender sender;
    private SqlHelper sqlHelper;

    public AskForOfflineMsg(SqlHelper sqlHelper) {
        this.sqlHelper = sqlHelper;
    }

    public ArrayList<HashMap<String, String>> ask(String account) {
        SqlSecurity sqlSecurity = sqlHelper.getSqlSecurity();
        String privateKey = sqlSecurity.getPrivateKey(account);
        sender = new MessageSender(privateKey);
        Message msg = getMsg();
        Message reply = sender.sendMessageSafely(msg, account);
        // fromWho;data
        String resultData = new String(reply.getData());
        if (resultData.equals(CHECK_FAIL)) {
            return null;
        }
        ArrayList<String> dataArrays = MessageCoder.decode(resultData);
        ArrayList<HashMap<String, String>> finalResult = new ArrayList<>();
        for (int i = 0; i < dataArrays.size(); i = i + 2) {
            HashMap<String, String> singleData = new HashMap<>();
            singleData.put("fromWho", dataArrays.get(i));
            singleData.put("whatMsg", dataArrays.get(i + 1));
            finalResult.add(singleData);
        }
        return finalResult;
    }

    private Message getMsg() {
        Message msg = new Message();
        msg.setKind(ASK_OFFLINE_CHAT_MSG);
        msg.setData("");
        return msg;
    }

    public static void main(String[] args) {
        SqlHelper sqlHelper = new SqlHelper();
        AskForOfflineMsg askForOfflineMsg = new AskForOfflineMsg(sqlHelper);
        ArrayList<HashMap<String, String>> result = askForOfflineMsg.ask("myh");
        for (HashMap<String, String> map : result) {
            System.out.println(map.get("fromWho") + ":" + map.get("whatMsg"));
        }
    }

}
