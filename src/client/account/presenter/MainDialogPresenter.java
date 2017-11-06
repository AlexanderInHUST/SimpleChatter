package client.account.presenter;

import client.account.view.MainDialog;
import client.base.BasePresenter;
import client.c2s.state.Logout;
import client.sql.SqlHelper;
import com.jgoodies.forms.factories.Borders;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by tangyifeng on 2017/11/6.
 * Email: yifengtang_hust@outlook.com
 */
public class MainDialogPresenter extends BasePresenter{

    private String account;
    private int p2pPort;
    private ArrayList<String> users;

    private Logout logout;

    public MainDialogPresenter(MainDialog mainDialog, SqlHelper sqlHelper, String account, int p2pPort, ArrayList<String> users) {
        super(mainDialog, sqlHelper);
        this.account = account;
        this.p2pPort = p2pPort;
        this.users = users;
        initialListeners();
        setCurrentUserText(account);
        setUsersData();
        logout = new Logout(sqlHelper);
    }

    @Override
    public void removeLogo(JFrame jFrame) {
        MainDialog mainDialog = (MainDialog) jFrame;
        mainDialog.getDialogPane().removePropertyChangeListener(mainDialog.getDialogPane().getPropertyChangeListeners()[0]);
        mainDialog.getDialogPane().setBorder(Borders.createEmptyBorder("9dlu, 9dlu, 9dlu, 9dlu"));
    }

    @Override
    public void initialListeners() {
        MainDialog mainDialog = (MainDialog) getFrame();
        mainDialog.getEndButton().addActionListener(getLogoutListener());
    }

    @SuppressWarnings("unchecked")
    private void setUsersData() {
        MainDialog mainDialog = (MainDialog) getFrame();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (int i = 0; i < users.size(); i += 2) {
            listModel.addElement(users.get(i) + "：" + (users.get(i + 1).equals("y") ? "在线中" : "离线中"));
        }
        mainDialog.getUsersList().setModel(listModel);
    }

    private ActionListener getLogoutListener() {
        return (ActionEvent) -> {
            MainDialog mainDialog = (MainDialog) getFrame();
            boolean result = logout.logout(account);
            if (result) {
                JOptionPane.showMessageDialog(null, "登出成功！",
                        "信息", JOptionPane.WARNING_MESSAGE);
                mainDialog.setVisible(false);
                mainDialog.dispose();
                System.exit(0);
            } else {
                JOptionPane.showMessageDialog(null, "登出失败！",
                        "信息", JOptionPane.WARNING_MESSAGE);
            }
        };
    }

    private void setCurrentUserText(String account) {
        MainDialog mainDialog = (MainDialog) getFrame();
        mainDialog.getCurrentUserText().setText(account);
    }
}
