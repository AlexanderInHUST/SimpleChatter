package client.account.presenter;

import client.account.view.ForgetPasswordDialog;
import client.account.view.LoginDialog;
import client.base.BasePresenter;
import client.c2s.account.ForgetPassword;
import client.sql.SqlHelper;
import com.jgoodies.forms.factories.Borders;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static message.MessageConst.CHECK_FAIL;
import static message.MessageConst.SUCCESS;

/**
 * Created by tangyifeng on 2017/11/6.
 * Email: yifengtang_hust@outlook.com
 */
public class ForgetPasswordDialogPresenter extends BasePresenter {

    private LoginDialog loginDialog;

    private MainDialogPresenter mainDialogPresenter;

    private ForgetPassword forgetPassword;

    public ForgetPasswordDialogPresenter(ForgetPasswordDialog forgetPasswordDialog, SqlHelper sqlHelper) {
        super(forgetPasswordDialog, sqlHelper);
        initialListeners();
        forgetPassword = new ForgetPassword();
    }

    @Override
    public void initialListeners() {
        ForgetPasswordDialog forgetPasswordDialog = (ForgetPasswordDialog) getFrame();
        forgetPasswordDialog.getOkButton().addActionListener(getChangeListener());
        forgetPasswordDialog.getCancelButton().addActionListener(getCancelListener());
    }

    @Override
    public void removeLogo(JFrame jFrame) {
        ForgetPasswordDialog forgetPasswordDialog = (ForgetPasswordDialog) jFrame;
        forgetPasswordDialog.getDialogPane().removePropertyChangeListener(forgetPasswordDialog.getDialogPane().getPropertyChangeListeners()[0]);
        forgetPasswordDialog.getDialogPane().setBorder(Borders.createEmptyBorder("9dlu, 9dlu, 9dlu, 9dlu"));
    }

    private ActionListener getChangeListener() {
        return (ActionEvent e) -> {
            ForgetPasswordDialog forgetPasswordDialog = (ForgetPasswordDialog) getFrame();
            String account = forgetPasswordDialog.getAccountEditText().getText();
            String question = forgetPasswordDialog.getQuestionEditText().getText();
            String answer = forgetPasswordDialog.getAnswerEditText().getText();
            String password = forgetPasswordDialog.getPasswordEditText().getText();
            if (account.isEmpty() || question.isEmpty() || answer.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "请完整输入信息！",
                        "错误", JOptionPane.WARNING_MESSAGE);
            } else {
                String result = forgetPassword.forgetPassword(account, question, answer, password);
                if (result.equals(SUCCESS)) {
                    JOptionPane.showMessageDialog(null, "密码修改成功！",
                            "信息", JOptionPane.WARNING_MESSAGE);
                    loginDialog = new LoginDialog();
                    mainDialogPresenter = new MainDialogPresenter(loginDialog, getSqlHelper());
                    forgetPasswordDialog.setVisible(false);
                    forgetPasswordDialog.dispose();
                    loginDialog.setVisible(true);
                } else if (result.equals(CHECK_FAIL)) {
                    JOptionPane.showMessageDialog(null, "密码问题或答案错误！",
                            "错误", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "网络错误！",
                            "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        };
    }

    private ActionListener getCancelListener() {
        return (ActionEvent e) -> {
            ForgetPasswordDialog forgetPasswordDialog = (ForgetPasswordDialog) getFrame();
            loginDialog = new LoginDialog();
            mainDialogPresenter = new MainDialogPresenter(loginDialog, getSqlHelper());
            forgetPasswordDialog.setVisible(false);
            forgetPasswordDialog.dispose();
            loginDialog.setVisible(true);
        };
    }
}
