/*
 * Created by JFormDesigner on Mon Nov 06 22:59:35 SGT 2017
 */

package client.view;

import java.awt.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

/**
 * @author Yihan Mei
 */
public class ChatDialog extends JFrame {
    public ChatDialog() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Yihan Mei
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        chatDescription = new JTextPane();
        chatUserText = new JTextPane();
        scrollPane1 = new JScrollPane();
        chatDetailList = new JList();
        chatEditText = new JTextField();
        sendButton = new JButton();
        fileButton = new JButton();
        progressText = new JTextPane();
        exitButton = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("\u804a\u5929\u6846");
        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(Borders.createEmptyBorder("9dlu, 9dlu, 9dlu, 9dlu"));
            dialogPane.setMinimumSize(new Dimension(450, 600));
            dialogPane.setPreferredSize(new Dimension(450, 600));

            // JFormDesigner evaluation mark
            dialogPane.setBorder(new javax.swing.border.CompoundBorder(
                new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                    "JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
                    javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                    java.awt.Color.red), dialogPane.getBorder())); dialogPane.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new FormLayout(
                    "34dlu, $lcgap, 70dlu, $lcgap, 50dlu, $lcgap, 50dlu:grow",
                    "default, $lgap, default:grow, 2*($lgap, default)"));

                //---- chatDescription ----
                chatDescription.setEditable(false);
                chatDescription.setBackground(SystemColor.window);
                chatDescription.setText("\u5f53\u524d\u7528\u6237\uff1a");
                contentPanel.add(chatDescription, CC.xy(1, 1));

                //---- chatUserText ----
                chatUserText.setEditable(false);
                chatUserText.setBackground(SystemColor.window);
                chatUserText.setText("Sample");
                contentPanel.add(chatUserText, CC.xywh(3, 1, 5, 1));

                //======== scrollPane1 ========
                {
                    scrollPane1.setViewportView(chatDetailList);
                }
                contentPanel.add(scrollPane1, CC.xywh(1, 3, 7, 1, CC.DEFAULT, CC.FILL));
                contentPanel.add(chatEditText, CC.xywh(1, 5, 3, 1));

                //---- sendButton ----
                sendButton.setText("\u53d1\u9001");
                contentPanel.add(sendButton, CC.xy(5, 5));

                //---- fileButton ----
                fileButton.setText("\u53d1\u9001\u6587\u4ef6");
                contentPanel.add(fileButton, CC.xy(7, 5));

                //---- progressText ----
                progressText.setEditable(false);
                progressText.setBackground(SystemColor.window);
                contentPanel.add(progressText, CC.xywh(1, 7, 5, 1));

                //---- exitButton ----
                exitButton.setText("\u9000\u51fa");
                contentPanel.add(exitButton, CC.xy(7, 7));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Yihan Mei
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JTextPane chatDescription;
    private JTextPane chatUserText;
    private JScrollPane scrollPane1;
    private JList chatDetailList;
    private JTextField chatEditText;
    private JButton sendButton;
    private JButton fileButton;
    private JTextPane progressText;
    private JButton exitButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables


    public JPanel getDialogPane() {
        return dialogPane;
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    public JTextPane getChatDescription() {
        return chatDescription;
    }

    public JTextPane getChatUserText() {
        return chatUserText;
    }

    public JScrollPane getScrollPane1() {
        return scrollPane1;
    }

    public JList getChatDetailList() {
        return chatDetailList;
    }

    public JTextField getChatEditText() {
        return chatEditText;
    }

    public JButton getSendButton() {
        return sendButton;
    }

    public JButton getFileButton() {
        return fileButton;
    }

    public JTextPane getProgressText() {
        return progressText;
    }

    public JButton getExitButton() {
        return exitButton;
    }
}
