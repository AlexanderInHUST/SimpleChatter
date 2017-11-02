package server.sql.detail;

import java.sql.Connection;

/**
 * Created by tangyifeng on 2017/11/2.
 * Email: yifengtang_hust@outlook.com
 */
public class SqlChat {

    private Connection connection;

    public SqlChat(Connection connection) {
        this.connection = connection;
    }

}
