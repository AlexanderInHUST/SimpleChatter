package client.account.presenter;

import client.account.view.LoginDialog;
import client.account.view.RegisterDialog;
import client.base.BasePresenter;
import client.c2s.account.LoginCheck;
import client.c2s.account.RegisterMsg;
import client.c2s.state.Login;
import client.sql.SqlHelper;
import com.jgoodies.forms.factories.Borders;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by tangyifeng on 2017/11/6.
 * Email: yifengtang_hust@outlook.com
 */
public class RegisterDialogPresenter extends BasePresenter {

    private LoginDialog loginDialog;

    private MainDialogPresenter mainDialogPresenter;

    private RegisterMsg registerMsg;

    public RegisterDialogPresenter(RegisterDialog registerDialog, SqlHelper sqlHelper) {
        super(registerDialog, sqlHelper);
        initialListeners();
        registerMsg = new RegisterMsg(sqlHelper);
    }

    @Override
    public void initialListeners() {
        RegisterDialog registerDialog = (RegisterDialog) getFrame();
        registerDialog.getOkButton().addActionListener(getRegisterListener());
        registerDialog.getCancelButton().addActionListener(getCancelListener());
    }

    @Override
    public void removeLogo(JFrame jFrame) {
        RegisterDialog registerDialog = (RegisterDialog) jFrame;
        registerDialog.getDialogPane().removePropertyChangeListener(registerDialog.getDialogPane().getPropertyChangeListeners()[0]);
        registerDialog.getDialogPane().setBorder(Borders.createEmptyBorder("9dlu, 9dlu, 9dlu, 9dlu"));
    }

    private ActionListener getRegisterListener() {
        return (ActionEvent e) -> {
            RegisterDialog registerDialog = (RegisterDialog) getFrame();
            String account = registerDialog.getAccountEditText().getText();
            String password = registerDialog.getPasswordEditText().getText();
            String question = registerDialog.getQuestionEditText().getText();
            String answer = registerDialog.getAnswerEditText().getText();
            if (account.isEmpty() || password.isEmpty() || question.isEmpty() || account.isEmpty()) {
                JOptionPane.showMessageDialog(null, "请完整输入信息！",
                        "错误", JOptionPane.WARNING_MESSAGE);
            } else {
                boolean result = registerMsg.register(account, password, question, answer);
                if (result) {
                    JOptionPane.showMessageDialog(null, "注册成功！",
                            "信息", JOptionPane.WARNING_MESSAGE);
                    loginDialog = new LoginDialog();
                    mainDialogPresenter = new MainDialogPresenter(loginDialog, getSqlHelper());
                    registerDialog.setVisible(false);
                    registerDialog.dispose();
                    loginDialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "注册失败！",
                            "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        };
    }

    private ActionListener getCancelListener() {
        return (ActionEvent e) -> {
            RegisterDialog registerDialog = (RegisterDialog) getFrame();
            loginDialog = new LoginDialog();
            mainDialogPresenter = new MainDialogPresenter(loginDialog, getSqlHelper());
            registerDialog.setVisible(false);
            registerDialog.dispose();
            loginDialog.setVisible(true);
        };
    }


}
