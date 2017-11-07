package client.presenter;

import client.c2s.chat.CheckState;
import client.c2s.chat.SendOffline;
import client.p2p.client.presenter.SendFile;
import client.p2p.client.presenter.SendMessage;
import client.view.ChatDialog;
import client.base.BasePresenter;
import client.sql.SqlHelper;
import client.view.MainDialog;
import com.jgoodies.forms.factories.Borders;
import udp.TransmitFile;

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

    private MainDialogPresenter mainDialogPresenter;

    private SendOffline sendOffline;
    private CheckState checkState;
    private SendFile sendFile;
    private SendMessage sendMessage;

    public ChatDialogPresenter(ChatDialog chatDialog, SqlHelper sqlHelper, MainDialogPresenter mainDialogPresenter, String account,
                               String withWhom) {
        super(chatDialog, sqlHelper);
        this.mainDialogPresenter = mainDialogPresenter;
        this.account = account;
        this.withWhom = withWhom;
        initialListeners();
        sendOffline = new SendOffline(sqlHelper);
        checkState = new CheckState(sqlHelper);
        sendFile = new SendFile();
        sendMessage = new SendMessage();

        chatDialog.getChatUserText().setText(withWhom);
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

    private ActionListener getExitListener() {
        return (ActionEvent e) -> {
            ChatDialog chatDialog = (ChatDialog) getFrame();
            chatDialog.setVisible(false);
            chatDialog.dispose();
            mainDialogPresenter.removeChatDialog(account);
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
                chatDialog.getChatEditText().setText("");
            }
        };
    }
}
