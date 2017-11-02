package server.sql;

/**
 * Created by tangyifeng on 2017/10/29.
 * Email: yifengtang_hust@outlook.com
 */
public class SqlConst {

    public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public static final String JDBC_DB_ADDRESS = "jdbc:mysql://localhost/chatter_server?autoReconnect=true&useSSL=false";

    public static final String JDBC_USER = "root";
    public static final String JDBC_PASSWORD = "39160816q";

    // Account
    public static final String DB_ACCOUNT_INSERT = "INSERT INTO ACCOUNT"
            + "(ACCOUNT, PASSWORD, P_KEY, PWD_QUESTION, PWD_ANSWER) VALUES"
            + "(?,?,?,?,?)";
    public static final String DB_ACCOUNT_GET = "SELECT * FROM ACCOUNT WHERE "
            + "ACCOUNT=?";
    public static final String DB_ACCOUNT_EDIT_PASSWORD = "UPDATE ACCOUNT SET "
            + "PASSWORD=? WHERE ACCOUNT=?";

    // State
    public static final String DB_STATE_INSERT = "INSERT INTO USER_STATE"
            + "(ACCOUNT, IP_ADDRESS, PORT) VALUES"
            + "(?,?,?)";
    public static final String DB_STATE_DELETE = "DELETE FROM USER_STATE WHERE "
            + "ACCOUNT=?";
    public static final String DB_STATE_GET = "SELECT * FROM USER_STATE WHERE 1=1";


}
