package server.sql;

import server.sql.detail.SqlAccount;
import server.sql.detail.SqlChat;
import server.sql.detail.SqlSecurity;
import server.sql.detail.SqlState;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import static server.sql.SqlConst.*;

/**
 * Created by tangyifeng on 2017/10/29.
 * Email: yifengtang_hust@outlook.com
 */
public class SqlHelper {

    private Connection dbConnection;
    private static SqlAccount sqlAccount;
    private static SqlState sqlState;
    private static SqlSecurity sqlSecurity;
    private static SqlChat sqlChat;

    public SqlHelper() {
        initialSqlHelper();
    }

    private void initialSqlHelper() {
        try {
            Class.forName(JDBC_DRIVER);
            dbConnection = DriverManager.getConnection(JDBC_DB_ADDRESS, JDBC_USER, JDBC_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public SqlAccount getSqlAccount() {
        if (sqlAccount == null) {
            sqlAccount = new SqlAccount(dbConnection);
        }
        return sqlAccount;
    }

    public SqlState getSqlState() {
        if (sqlState == null) {
            sqlState = new SqlState(dbConnection);
        }
        return sqlState;
    }

    public SqlSecurity getSqlSecurity() {
        if (sqlSecurity == null) {
            sqlSecurity = new SqlSecurity(dbConnection);
        }
        return sqlSecurity;
    }

    public SqlChat getSqlChat() {
        if (sqlChat == null) {
            sqlChat = new SqlChat(dbConnection);
        }
        return sqlChat;
    }

    public void shutdownSqlHelper() {
        try {
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SqlHelper sqlHelper = new SqlHelper();
        SqlChat sqlChat = sqlHelper.getSqlChat();
        sqlChat.insertOfflineMsg("tyf", "myh", "hello!".getBytes());
        sqlChat.insertOfflineMsg("tyf", "myh", "bye!".getBytes());
        ArrayList<HashMap<String, Object>> result = sqlChat.getOfflineMsg("myh");
        for (HashMap<String, Object> map : result) {
            System.out.println(map.get("fromWho") + ":" + new String((byte[]) map.get("whatMsg")));
        }
    }

}
