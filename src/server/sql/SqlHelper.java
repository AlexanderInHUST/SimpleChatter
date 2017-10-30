package server.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static server.sql.SqlConst.*;

/**
 * Created by tangyifeng on 2017/10/29.
 * Email: yifengtang_hust@outlook.com
 */
public class SqlHelper {

    private Connection dbConnection;

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

    public static void main(String[] args) {
        SqlHelper sqlHelper = new SqlHelper();

    }

}
