package server.sql.detail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static server.sql.SqlConst.DB_ACCOUNT_GET;

/**
 * Created by tangyifeng on 2017/10/31.
 * Email: yifengtang_hust@outlook.com
 */
public class SqlSecurity {

    private Connection connection;

    public SqlSecurity(Connection connection) {
        this.connection = connection;
    }

    public String getPublicKey(String account) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DB_ACCOUNT_GET);
            preparedStatement.setString(1, account);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            String publicKey = resultSet.getString("p_key");
            resultSet.close();
            preparedStatement.close();
            return publicKey;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
