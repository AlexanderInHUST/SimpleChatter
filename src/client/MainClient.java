package client;

import client.account.presenter.MainDialogPresenter;
import client.account.view.LoginDialog;

/**
 * Created by tangyifeng on 2017/10/30.
 * Email: yifengtang_hust@outlook.com
 */
public class MainClient {

    private LoginDialog mainDialog;
    private MainDialogPresenter mainDialogPresenter;

    public MainClient() {
        mainDialog = new LoginDialog();
        mainDialogPresenter = new MainDialogPresenter(mainDialog);
    }

    public static void main(String[] args) {
        MainClient mainClient = new MainClient();
        mainClient.mainDialog.setVisible(true);
    }

}
