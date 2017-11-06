package client.account.presenter;

import client.account.view.ForgetPasswordDialog;
import client.base.BasePresenter;
import client.sql.SqlHelper;
import com.jgoodies.forms.factories.Borders;

import javax.swing.*;

/**
 * Created by tangyifeng on 2017/11/6.
 * Email: yifengtang_hust@outlook.com
 */
public class ForgetPasswordDialogPresenter extends BasePresenter {

    public ForgetPasswordDialogPresenter(ForgetPasswordDialog forgetPasswordDialog, SqlHelper sqlHelper) {
        super(forgetPasswordDialog, sqlHelper);
    }

    @Override
    public void initialListeners() {

    }

    @Override
    public void removeLogo(JFrame jFrame) {
        ForgetPasswordDialog forgetPasswordDialog = (ForgetPasswordDialog) jFrame;
        forgetPasswordDialog.getDialogPane().removePropertyChangeListener(forgetPasswordDialog.getDialogPane().getPropertyChangeListeners()[0]);
        forgetPasswordDialog.getDialogPane().setBorder(Borders.createEmptyBorder("9dlu, 9dlu, 9dlu, 9dlu"));
    }
}
