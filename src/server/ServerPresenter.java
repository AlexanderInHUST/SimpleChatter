package server;

import com.jgoodies.forms.factories.Borders;
import server.unseen.MainServerCmd;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by tangyifeng on 2017/11/7.
 * Email: yifengtang_hust@outlook.com
 */
public class ServerPresenter {

    private ServerDialog serverDialog;
    private MainServerCmd mainServerCmd;
    private DefaultListModel<String> listModel;

    public ServerPresenter(ServerDialog serverDialog) {
        this.serverDialog = serverDialog;
        mainServerCmd = new MainServerCmd(this);
        listModel = new DefaultListModel<>();
        removeLogo();
        initialListeners();
    }

    private void removeLogo() {
        serverDialog.getDialogPane().removePropertyChangeListener(serverDialog.getDialogPane().getPropertyChangeListeners()[0]);
        serverDialog.getDialogPane().setBorder(Borders.createEmptyBorder("9dlu, 9dlu, 9dlu, 9dlu"));
    }

    private void initialListeners() {
        serverDialog.getOkButton().addActionListener(getStartListener());
        serverDialog.getCancelButton().addActionListener(getCloseListener());
    }

    private ActionListener getStartListener() {
        return (ActionEvent e) -> {
            if (!mainServerCmd.isRunning()) {
                mainServerCmd.start();
                freshMsg("Server is running now");
            } else {
                JOptionPane.showMessageDialog(null, "Server已经开始运行！",
                        "错误", JOptionPane.WARNING_MESSAGE);
            }
        };
    }

    private ActionListener getCloseListener() {
        return (ActionEvent e) -> {
            if (mainServerCmd.isRunning()) {
                mainServerCmd.close();
            }
            serverDialog.setVisible(false);
            serverDialog.dispose();
            System.exit(0);
        };
    }

    @SuppressWarnings("unchecked")
    public void freshMsg(String msg) {
        listModel.addElement(msg);
        serverDialog.getCmdText().setModel(listModel);
    }

}
