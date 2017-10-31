package server.sql.detail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static server.sql.SqlConst.DB_ACCOUNT_EDIT_PASSWORD;
import static server.sql.SqlConst.DB_ACCOUNT_GET;
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

    public boolean insertAccount(String account, String password, String publicKey, String pwdQuestion, String pwdAnswer) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DB_ACCOUNT_INSERT);
            preparedStatement.setString(1, account);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, publicKey);
            preparedStatement.setString(4, pwdQuestion);
            preparedStatement.setString(5, pwdAnswer);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkPassword(String account, String password) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DB_ACCOUNT_GET);
            preparedStatement.setString(1, account);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return false;
            }
            String correctPassword = resultSet.getString("password");
            boolean result = correctPassword.equals(password);
            resultSet.close();
            preparedStatement.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkPwdAnswer(String account, String pwdQuestion, String pwdAnswer) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DB_ACCOUNT_GET);
            preparedStatement.setString(1, account);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return false;
            }
            String correctQue = resultSet.getString("pwd_question");
            String correctAns = resultSet.getString("pwd_answer");
            boolean result = correctQue.equals(pwdQuestion) && correctAns.equals(pwdAnswer);
            resultSet.close();
            preparedStatement.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean editPwd(String account, String password) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DB_ACCOUNT_EDIT_PASSWORD);
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, account);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
