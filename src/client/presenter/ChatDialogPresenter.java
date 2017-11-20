package client.presenter;

import client.c2s.chat.CheckState;
import client.c2s.chat.SendOffline;
import client.p2p.client.presenter.SendMessage;
import client.view.ChatDialog;
import client.base.BasePresenter;
import client.sql.SqlHelper;
import com.jgoodies.forms.factories.Borders;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


/**
 * Created by tangyifeng on 2017/11/7.
 * Email: yifengtang_hust@outlook.com
 */
public class ChatDialogPresenter extends BasePresenter {

    private String account;
    private String withWhom;
    private int filePort;
    private int lastCount = 0;
    private long lastTime = 0;
    private DefaultListModel<String> msgListModel = new DefaultListModel<>();

    private MainDialogPresenter mainDialogPresenter;

    private SendOffline sendOffline;
    private CheckState checkState;
    private SendMessage sendMessage;


    public ChatDialogPresenter(ChatDialog chatDialog, SqlHelper sqlHelper, MainDialogPresenter mainDialogPresenter, String account,
                               String withWhom, int filePort) {
        super(chatDialog, sqlHelper);
        this.mainDialogPresenter = mainDialogPresenter;
        this.account = account;
        this.withWhom = withWhom;
        this.filePort = filePort;
        initialListeners();
        sendOffline = new SendOffline(sqlHelper);
        checkState = new CheckState(sqlHelper);
        sendMessage = new SendMessage();
        msgListModel = new DefaultListModel<>();

        chatDialog.getChatUserText().setText(withWhom);
    }

    private void askForFresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }

    @Override
    public void removeLogo(JFrame jFrame) {
        ChatDialog chatDialog = (ChatDialog) jFrame;
        chatDialog.getDialogPane().removePropertyChangeListener(chatDialog.getDialogPane().getPropertyChangeListeners()[0]);
        chatDialog.getDialogPane().setBorder(Borders.createEmptyBorder("9dlu, 9dlu, 9dlu, 9dlu"));
    }

    @Override
    public void initialListeners() {
        ChatDialog chatDialog = (ChatDialog) getFrame();
        chatDialog.getExitButton().addActionListener(getExitListener());
        chatDialog.getSendButton().addActionListener(getSendListener());
    }

    @SuppressWarnings("unchecked")
    public void addMsgToList(String account, String msg) {
        ChatDialog chatDialog = (ChatDialog) getFrame();
        msgListModel.addElement(account + "：" + msg);
        chatDialog.getChatDetailList().setModel(msgListModel);
    }

    private ActionListener getExitListener() {
        return (ActionEvent e) -> {
            ChatDialog chatDialog = (ChatDialog) getFrame();
            chatDialog.setVisible(false);
            chatDialog.dispose();
            mainDialogPresenter.removeChatDialog(withWhom);
        };
    }

    private ActionListener getSendListener() {
        return (ActionEvent e) -> {
            ChatDialog chatDialog = (ChatDialog) getFrame();
            String msg = chatDialog.getChatEditText().getText();
            if (msg.isEmpty()) {
                JOptionPane.showMessageDialog(null, "消息不能为空！",
                        "错误", JOptionPane.WARNING_MESSAGE);
            } else {
                ArrayList<String> isOnline = checkState.check(account, withWhom);
                if (isOnline == null) {
                    sendOffline.send(account, withWhom, msg);
                } else {
                    sendMessage.send(account, isOnline.get(0), Integer.parseInt(isOnline.get(1)), msg);
                }
                addMsgToList(account, msg);
                chatDialog.getChatEditText().setText("");
            }
        };
    }
}
