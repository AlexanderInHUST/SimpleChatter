package client;

import client.presenter.LoginDialogPresenter;
import client.view.LoginDialog;

/**
 * Created by tangyifeng on 2017/10/30.
 * Email: yifengtang_hust@outlook.com
 */
public class MainClient {

    private LoginDialog mainDialog;
    private LoginDialogPresenter loginDialogPresenter;

    public MainClient() {
        mainDialog = new LoginDialog();
        loginDialogPresenter = new LoginDialogPresenter(mainDialog);
    }

    public static void main(String[] args) {
        MainClient mainClient = new MainClient();
        mainClient.mainDialog.setVisible(true);
    }

}
