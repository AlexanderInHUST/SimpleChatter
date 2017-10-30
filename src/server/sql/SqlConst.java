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

    public static final String DB_ACCOUNT_INSERT = "INSERT INTO ACCOUNT"
            + "(ACCOUNT, PASSWORD, P_KEY, PWD_QUESTION, PWD_ANSWER) VALUES"
            + "(?,?,?,?,?)";
}
