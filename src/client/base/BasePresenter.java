package client.base;

import client.sql.SqlHelper;

import javax.swing.*;

/**
 * Created by tangyifeng on 2017/11/6.
 * Email: yifengtang_hust@outlook.com
 */
public abstract class BasePresenter {

    private JFrame frame;
    private SqlHelper sqlHelper;

    public BasePresenter(JFrame frame, SqlHelper sqlHelper) {
        this.frame = frame;
        this.sqlHelper = sqlHelper;
        removeLogo(frame);
    }

    public abstract void removeLogo(JFrame jFrame);
    public abstract void initialListeners();

    public JFrame getFrame() {
        return frame;
    }

    public SqlHelper getSqlHelper() {
        return sqlHelper;
    }
}
