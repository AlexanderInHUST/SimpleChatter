package server.sql.detail;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import static server.sql.SqlConst.DB_CHAT_GET;
import static server.sql.SqlConst.DB_CHAT_INSERT;

/**
 * Created by tangyifeng on 2017/11/2.
 * Email: yifengtang_hust@outlook.com
 */
public class SqlChat {

    private Connection connection;

    public SqlChat(Connection connection) {
        this.connection = connection;
    }

    public boolean insertOfflineMsg(String fromWho, String toWho, byte[] whatMsg) {
        try {
            Blob whatMsgBlob = new SerialBlob(whatMsg);
            PreparedStatement preparedStatement = connection.prepareStatement(DB_CHAT_INSERT);
            preparedStatement.setLong(1, System.currentTimeMillis());
            preparedStatement.setString(2, fromWho);
            preparedStatement.setString(3, toWho);
            preparedStatement.setBlob(4, whatMsgBlob);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<HashMap<String, Object>> getOfflineMsg(String toWho) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DB_CHAT_GET);
            preparedStatement.setString(1, toWho);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<HashMap<String, Object>> result = new ArrayList<>();
            while (resultSet.next()) {
                HashMap<String, Object> singleLine = new HashMap<>();
                singleLine.put("fromWho", resultSet.getString("from_who"));
                singleLine.put("toWho", resultSet.getString("to_who"));
                Blob whatMsg = resultSet.getBlob("what_msg");
                singleLine.put("whatMsg", whatMsg.getBytes(1, (int) whatMsg.length()));
                result.add(singleLine);
            }
            preparedStatement.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
