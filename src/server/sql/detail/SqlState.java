package server.sql.detail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static server.sql.SqlConst.*;

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

    public boolean deleteLogin(String account) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DB_STATE_DELETE);
            preparedStatement.setString(1, account);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<String> getOnlineUsers() {
        try {
            ArrayList<String> result = new ArrayList<>();
            PreparedStatement preparedStatement = connection.prepareStatement(DB_STATE_GET);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(resultSet.getString("account"));
            }
            preparedStatement.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> checkOnline(String account) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DB_STATE_CHECK);
            preparedStatement.setString(1, account);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean result = resultSet.next();
            if (!result) {
                return null;
            }
            ArrayList<String> userState = new ArrayList<>();
            userState.add(resultSet.getString("ip_address"));
            userState.add(resultSet.getString("port"));
            preparedStatement.close();
            return userState;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
