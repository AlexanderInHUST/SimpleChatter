package server.sql.detail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static server.sql.SqlConst.DB_STATE_INSERT;

/**
 * Created by tangyifeng on 2017/10/31.
 * Email: yifengtang_hust@outlook.com
 */
public class SqlState {

    private Connection connection;

    public SqlState(Connection connection) {
        this.connection = connection;
    }

    public boolean insertLogin(String account, String ipAddress, String port) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DB_STATE_INSERT);
            preparedStatement.setString(1, account);
            preparedStatement.setString(2, ipAddress);
            preparedStatement.setString(3, port);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
