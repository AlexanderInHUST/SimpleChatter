package server;

/**
 * Created by tangyifeng on 2017/11/7.
 * Email: yifengtang_hust@outlook.com
 */
public class MainServer {

    public static void main(String[] args) {
        ServerDialog serverDialog = new ServerDialog();
        ServerPresenter serverPresenter = new ServerPresenter(serverDialog);
        serverDialog.setVisible(true);
    }
}
