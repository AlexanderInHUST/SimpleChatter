package server.sql.detail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static server.sql.SqlConst.DB_ACCOUNT_INSERT;

/**
 * Created by tangyifeng on 2017/10/30.
 * Email: yifengtang_hust@outlook.com
 */
public class SqlAccount {

    private Connection connection;

    public SqlAccount(Connection connection) {
        this.connection = connection;
    }

    public void insertAccount(String account, String password, String publicKey, String pwdQuestion, String pwdAnswer) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DB_ACCOUNT_INSERT);
            preparedStatement.setString(1, account);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, publicKey);
            preparedStatement.setString(4, pwdQuestion);
            preparedStatement.setString(5, pwdAnswer);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}