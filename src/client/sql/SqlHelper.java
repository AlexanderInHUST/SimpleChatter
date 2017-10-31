package client.sql;

import client.sql.detail.SqlAccount;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static client.sql.SqlConst.*;

/**
 * Created by tangyifeng on 2017/10/31.
 * Email: yifengtang_hust@outlook.com
 */
public class SqlHelper {

    private Connection dbConnection;
    private static SqlAccount sqlAccount;

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

    public void shutdownSqlHelper() {
        try {
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SqlHelper sqlHelper = new SqlHelper();
        SqlAccount sqlAccount = sqlHelper.getSqlAccount();
        sqlAccount.insertAccount("test", "test");
    }
}
