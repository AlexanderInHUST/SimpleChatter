package client.presenter;

import client.c2s.chat.AskForOfflineMsg;
import client.p2p.server.presenter.RecvChatPresenter;
import client.view.ChatDialog;
import client.view.MainDialog;
import client.base.BasePresenter;
import client.c2s.state.Logout;
import client.p2p.server.P2PServer;
import client.sql.SqlHelper;
import com.jgoodies.forms.factories.Borders;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import static client.p2p.P2PConst.DEFAULT_P2P_TIMEOUT;

/**
 * Created by tangyifeng on 2017/11/6.
 * Email: yifengtang_hust@outlook.com
 */
public class MainDialogPresenter extends BasePresenter{

    private String account;
    private int p2pPort;
    private int filePort;
    private ArrayList<String> users;
    private HashMap<String, ChatDialog> chatDialogHashMap;
    private HashMap<String, ChatDialogPresenter> chatDialogPresenterHashMap;
    private DefaultListModel<String> listModel;
    private RecvChatPresenter recvChatPresenter;

    private Logout logout;
    private P2PServer p2PServer;
    private AskForOfflineMsg askForOfflineMsg;

    public MainDialogPresenter(MainDialog mainDialog, SqlHelper sqlHelper, String account, int p2pPort, int filePort, ArrayList<String> users) {
        super(mainDialog, sqlHelper);
        this.account = account;
        this.p2pPort = p2pPort;
        this.users = users;
        this.filePort = filePort;
        p2PServer = new P2PServer(p2pPort, DEFAULT_P2P_TIMEOUT);
        chatDialogHashMap = new HashMap<>();
        chatDialogPresenterHashMap = new HashMap<>();
        recvChatPresenter = new RecvChatPresenter(this);

        initialListeners();
        setCurrentUserText(account);
        setUsersData();
        logout = new Logout(sqlHelper);
        askForOfflineMsg = new AskForOfflineMsg(sqlHelper);
        ArrayList<HashMap<String, String>> offlineMsgArrays = askForOfflineMsg.ask(account);
        for (HashMap<String, String> offlineMsg : offlineMsgArrays) {
            addToChatDialog(offlineMsg.get("fromWho"), offlineMsg.get("whatMsg"));
        }
    }

    @Override
    public void removeLogo(JFrame jFrame) {
        MainDialog mainDialog = (MainDialog) jFrame;
        mainDialog.getDialogPane().removePropertyChangeListener(mainDialog.getDialogPane().getPropertyChangeListeners()[0]);
        mainDialog.getDialogPane().setBorder(Borders.createEmptyBorder("9dlu, 9dlu, 9dlu, 9dlu"));
        mainDialog.getUsersList().addMouseListener(getListMouseAdapter());
    }

    @Override
    public void initialListeners() {
        MainDialog mainDialog = (MainDialog) getFrame();
        mainDialog.getEndButton().addActionListener(getLogoutListener());
    }

    public void addToChatDialog(String withWhom, String s) {
        if (chatDialogHashMap.containsKey(withWhom)) {
            ChatDialogPresenter chatDialogPresenter = chatDialogPresenterHashMap.get(withWhom);
            chatDialogPresenter.addMsgToList(withWhom, s);
        } else {
            ChatDialog chatDialog = new ChatDialog();
            ChatDialogPresenter chatDialogPresenter = new ChatDialogPresenter(chatDialog, getSqlHelper(), this, account, withWhom, filePort);
            chatDialog.setVisible(true);
            chatDialogHashMap.put(withWhom, chatDialog);
            chatDialogPresenterHashMap.put(withWhom, chatDialogPresenter);
            chatDialogPresenter.addMsgToList(withWhom, s);
        }
    }

    public void removeChatDialog(String account) {
        chatDialogHashMap.remove(account);
        chatDialogPresenterHashMap.remove(account);
    }

    @SuppressWarnings("unchecked")
    private void setUsersData() {
        MainDialog mainDialog = (MainDialog) getFrame();
        listModel = new DefaultListModel<>();
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
                p2PServer.close();
                System.exit(0);
            } else {
                JOptionPane.showMessageDialog(null, "登出失败！",
                        "信息", JOptionPane.WARNING_MESSAGE);
            }
        };
    }

    private MouseAdapter getListMouseAdapter() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MainDialog mainDialog = (MainDialog) getFrame();
                if(mainDialog.getUsersList().getSelectedIndex() != -1) {
                    if(e.getClickCount() == 1) {
                        super.mouseClicked(e);
                    }
                    if(e.getClickCount() == 2) {
                        int num = mainDialog.getUsersList().getSelectedIndex();
                        String withWhom = listModel.getElementAt(num).split("：")[0];
                        ChatDialog chatDialog = new ChatDialog();
                        ChatDialogPresenter chatDialogPresenter = new ChatDialogPresenter(chatDialog, getSqlHelper(), MainDialogPresenter.this, account, withWhom, filePort);
                        chatDialog.setVisible(true);
                        chatDialogHashMap.put(withWhom, chatDialog);
                        chatDialogPresenterHashMap.put(withWhom, chatDialogPresenter);
                    }
                }
            }
        };
    }

    private void setCurrentUserText(String account) {
        MainDialog mainDialog = (MainDialog) getFrame();
        mainDialog.getCurrentUserText().setText(account);
    }
}
