package client.sql;

/**
 * Created by tangyifeng on 2017/10/31.
 * Email: yifengtang_hust@outlook.com
 */
public class SqlConst {

    public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public static final String JDBC_DB_ADDRESS = "jdbc:mysql://localhost/chatter_user?autoReconnect=true&useSSL=false";

    public static final String JDBC_USER = "root";
    public static final String JDBC_PASSWORD = "39160816q"; // b857738325

    public static final String DB_ACCOUNT_INSERT = "INSERT INTO ACCOUNT"
            + "(ACCOUNT, P_KEY) VALUES"
            + "(?,?)";
    public static final String DB_ACCOUNT_GET = "SELECT * FROM ACCOUNT WHERE "
            + "ACCOUNT=?";
}
