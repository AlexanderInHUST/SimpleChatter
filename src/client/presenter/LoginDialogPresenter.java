package client.presenter;

import client.view.ForgetPasswordDialog;
import client.view.LoginDialog;
import client.view.MainDialog;
import client.view.RegisterDialog;
import client.base.BasePresenter;
import client.c2s.account.LoginCheck;
import client.c2s.state.Login;
import client.sql.SqlHelper;
import com.jgoodies.forms.factories.Borders;
import util.WebUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by tangyifeng on 2017/11/6.
 * Email: yifengtang_hust@outlook.com
 */
public class LoginDialogPresenter extends BasePresenter {

    private ForgetPasswordDialog forgetPasswordDialog;
    private RegisterDialog registerDialog;
    private MainDialog mainDialog;

    private ForgetPasswordDialogPresenter forgetPasswordDialogPresenter;
    private RegisterDialogPresenter registerDialogPresenter;
    private MainDialogPresenter mainDialogPresenter;

    private LoginCheck loginCheck;
    private Login login;

    public LoginDialogPresenter(LoginDialog mainDialog) {
        super(mainDialog, new SqlHelper());
        initialListeners();
        loginCheck = new LoginCheck();
        login = new Login(getSqlHelper());
    }

    public LoginDialogPresenter(LoginDialog mainDialog, SqlHelper sqlHelper) {
        super(mainDialog, sqlHelper);
        initialListeners();
        loginCheck = new LoginCheck();
        login = new Login(getSqlHelper());
    }

    @Override
    public void initialListeners() {
        LoginDialog loginDialog = (LoginDialog) getFrame();
        loginDialog.getOkButton().addActionListener(getLoginListener());
        loginDialog.getRegisterButton().addActionListener(getRegisterListener());
        loginDialog.getForgetButton().addActionListener(getForgetListener());
    }

    @Override
    public void removeLogo(JFrame jFrame) {
        LoginDialog loginDialog = (LoginDialog) jFrame;
        loginDialog.getDialogPane().removePropertyChangeListener(loginDialog.getDialogPane().getPropertyChangeListeners()[0]);
        loginDialog.getDialogPane().setBorder(Borders.createEmptyBorder("9dlu, 9dlu, 9dlu, 9dlu"));
    }

    private ActionListener getLoginListener() {
        return (ActionEvent e) -> {
            LoginDialog loginDialog = (LoginDialog) getFrame();
            String account = loginDialog.getAccountEditText().getText();
            String password = loginDialog.getPasswordEditText().getText();
            String portString = loginDialog.getPortEditText().getText();
            String filePortString = loginDialog.getFilePortEditText().getText();
            if (account.isEmpty() || password.isEmpty() || portString.isEmpty() || filePortString.isEmpty()) {
                JOptionPane.showMessageDialog(null, "请完整输入信息！",
                        "错误", JOptionPane.WARNING_MESSAGE);
            } else {
                boolean checkResult = loginCheck.loginCheck(account, password);
                if (!checkResult) {
                    JOptionPane.showMessageDialog(null, "账号或者密码错误！",
                            "错误", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int port = Integer.parseInt(portString);
                int filePort = Integer.parseInt(filePortString);
                ArrayList<String> onlineGuys = login.login(account, WebUtil.getLocalHostIP(), port);
                if (onlineGuys == null) {
                    JOptionPane.showMessageDialog(null, "网络错误！",
                            "错误", JOptionPane.WARNING_MESSAGE);
                } else {
                    mainDialog = new MainDialog();
                    mainDialogPresenter = new MainDialogPresenter(mainDialog, getSqlHelper(), account, port, filePort, onlineGuys);
                    loginDialog.setVisible(false);
                    loginDialog.dispose();
                    mainDialog.setVisible(true);
                }
            }
        };
    }

    private ActionListener getRegisterListener() {
        return (ActionEvent e) -> {
            LoginDialog loginDialog = (LoginDialog) getFrame();
            registerDialog = new RegisterDialog();
            registerDialogPresenter = new RegisterDialogPresenter(registerDialog, getSqlHelper());
            loginDialog.setVisible(false);
            loginDialog.dispose();
            registerDialog.setVisible(true);
        };
    }

    private ActionListener getForgetListener() {
        return (ActionEvent e) -> {
            LoginDialog loginDialog = (LoginDialog) getFrame();
            forgetPasswordDialog = new ForgetPasswordDialog();
            forgetPasswordDialogPresenter = new ForgetPasswordDialogPresenter(forgetPasswordDialog, getSqlHelper());
            loginDialog.setVisible(false);
            loginDialog.dispose();
            forgetPasswordDialog.setVisible(true);
        };
    }
}
