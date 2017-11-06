/*
 * Created by JFormDesigner on Mon Nov 06 16:22:04 SGT 2017
 */

package client.account.view;

import java.awt.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

/**
 * @author Yihan Mei
 */
public class RegisterDialog extends JFrame {
    public RegisterDialog() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Yihan Mei
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        registerDescription = new JTextPane();
        accountText = new JTextPane();
        accountEditText = new JTextField();
        passwordText = new JTextPane();
        passwordEditText = new JPasswordField();
        questionText = new JTextPane();
        questionEditText = new JTextField();
        answerText = new JTextPane();
        answerEditText = new JTextField();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setTitle("\u7528\u6237\u6ce8\u518c");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(Borders.createEmptyBorder("9dlu, 9dlu, 9dlu, 9dlu"));

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
                    "default, $lcgap, 135dlu:grow",
                    "4*(default, $lgap), default"));

                //---- registerDescription ----
                registerDescription.setEditable(false);
                registerDescription.setBackground(SystemColor.window);
                registerDescription.setText("\u8bf7\u8f93\u5165\u4ee5\u4e0b\u7684\u6ce8\u518c\u4fe1\u606f");
                contentPanel.add(registerDescription, CC.xywh(1, 1, 3, 1, CC.CENTER, CC.DEFAULT));

                //---- accountText ----
                accountText.setEditable(false);
                accountText.setBackground(SystemColor.window);
                accountText.setText("\u8d26\u53f7\uff1a");
                contentPanel.add(accountText, CC.xy(1, 3));
                contentPanel.add(accountEditText, CC.xy(3, 3, CC.FILL, CC.DEFAULT));

                //---- passwordText ----
                passwordText.setEditable(false);
                passwordText.setBackground(SystemColor.window);
                passwordText.setText("\u5bc6\u7801\uff1a");
                contentPanel.add(passwordText, CC.xy(1, 5));
                contentPanel.add(passwordEditText, CC.xy(3, 5, CC.FILL, CC.DEFAULT));

                //---- questionText ----
                questionText.setEditable(false);
                questionText.setBackground(SystemColor.window);
                questionText.setText("\u627e\u56de\u5bc6\u7801\u95ee\u9898\uff1a");
                contentPanel.add(questionText, CC.xy(1, 7));
                contentPanel.add(questionEditText, CC.xy(3, 7, CC.FILL, CC.DEFAULT));

                //---- answerText ----
                answerText.setEditable(false);
                answerText.setBackground(SystemColor.window);
                answerText.setText("\u627e\u56de\u5bc6\u7801\u7b54\u6848\uff1a");
                contentPanel.add(answerText, CC.xy(1, 9));
                contentPanel.add(answerEditText, CC.xy(3, 9, CC.FILL, CC.DEFAULT));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(Borders.createEmptyBorder("4dlu, 0dlu, 0dlu, 0dlu"));
                buttonBar.setLayout(new FormLayout(
                    "$glue, $button, $rgap, $button",
                    "pref"));

                //---- okButton ----
                okButton.setText("\u6ce8\u518c");
                buttonBar.add(okButton, CC.xy(2, 1));

                //---- cancelButton ----
                cancelButton.setText("\u53d6\u6d88");
                buttonBar.add(cancelButton, CC.xy(4, 1));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
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
    private JTextPane registerDescription;
    private JTextPane accountText;
    private JTextField accountEditText;
    private JTextPane passwordText;
    private JPasswordField passwordEditText;
    private JTextPane questionText;
    private JTextField questionEditText;
    private JTextPane answerText;
    private JTextField answerEditText;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables


    public JPanel getDialogPane() {
        return dialogPane;
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    public JTextPane getRegisterDescription() {
        return registerDescription;
    }

    public JTextPane getAccountText() {
        return accountText;
    }

    public JTextField getAccountEditText() {
        return accountEditText;
    }

    public JTextPane getPasswordText() {
        return passwordText;
    }

    public JTextField getPasswordEditText() {
        return passwordEditText;
    }

    public JTextPane getQuestionText() {
        return questionText;
    }

    public JTextField getQuestionEditText() {
        return questionEditText;
    }

    public JTextPane getAnswerText() {
        return answerText;
    }

    public JTextField getAnswerEditText() {
        return answerEditText;
    }

    public JPanel getButtonBar() {
        return buttonBar;
    }

    public JButton getOkButton() {
        return okButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }
}
