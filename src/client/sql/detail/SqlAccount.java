package client.sql.detail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static client.sql.SqlConst.DB_ACCOUNT_INSERT;

/**
 * Created by tangyifeng on 2017/10/31.
 * Email: yifengtang_hust@outlook.com
 */
public class SqlAccount {

    private Connection connection;

    public SqlAccount(Connection connection) {
        this.connection = connection;
    }

    public void insertAccount(String account, String privateKey) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DB_ACCOUNT_INSERT);
            preparedStatement.setString(1, account);
            preparedStatement.setString(2, privateKey);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
