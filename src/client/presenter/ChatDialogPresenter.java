package client.presenter;

import client.c2s.chat.CheckState;
import client.c2s.chat.SendOffline;
import client.p2p.client.presenter.SendFile;
import client.p2p.client.presenter.SendMessage;
import client.p2p.server.presenter.RecvFilePresenter;
import client.view.ChatDialog;
import client.base.BasePresenter;
import client.sql.SqlHelper;
import client.view.MainDialog;
import com.jgoodies.forms.factories.Borders;
import udp.TransmitFile;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by tangyifeng on 2017/11/7.
 * Email: yifengtang_hust@outlook.com
 */
public class ChatDialogPresenter extends BasePresenter {

    private String account;
    private String withWhom;
    private int filePort;
    private DefaultListModel<String> msgListModel = new DefaultListModel<>();

    private MainDialogPresenter mainDialogPresenter;

    private SendOffline sendOffline;
    private CheckState checkState;
    private SendFile sendFile;
    private SendMessage sendMessage;

    private RecvFilePresenter recvFilePresenter;

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
        sendFile = new SendFile();
        sendMessage = new SendMessage();
        msgListModel = new DefaultListModel<>();
        recvFilePresenter = new RecvFilePresenter(filePort);

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
        chatDialog.getFileButton().addActionListener(getFileListener());
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

    private ActionListener getFileListener() {
        return (ActionEvent e) -> {
            ChatDialog chatDialog = (ChatDialog) getFrame();
            ArrayList<String> isOnline = checkState.check(account, withWhom);
            if (isOnline == null) {
                JOptionPane.showMessageDialog(null, "对方不在线！",
                        "错误", JOptionPane.WARNING_MESSAGE);
            } else {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.showOpenDialog(new JPanel());
                File file = chooser.getSelectedFile();
                chatDialog.getProgressText().setText("文件发送中...");
                if (file.exists()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boolean result = sendFile.transmit(account, file.getAbsolutePath(), isOnline.get(0), Integer.parseInt(isOnline.get(1)), filePort);
                            if (result) {
                                JOptionPane.showMessageDialog(null, "发送成功！",
                                        "信息", JOptionPane.WARNING_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "发送错误！",
                                        "错误", JOptionPane.WARNING_MESSAGE);
                            }
                        }
                    }).start();
                } else {
                    JOptionPane.showMessageDialog(null, "请选择文件！",
                            "错误", JOptionPane.WARNING_MESSAGE);
                }
                chatDialog.getProgressText().setText("");
            }
        };
    }
}
