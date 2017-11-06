/*
 * Created by JFormDesigner on Mon Nov 06 10:53:25 SGT 2017
 */

package client.account;

import java.awt.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import info.clearthought.layout.*;

/**
 * @author Alex Tang
 */
public class LoginDialog extends JDialog {
    public LoginDialog(Frame owner) {
        super(owner);
        initComponents();
    }

    public LoginDialog(Dialog owner) {
        super(owner);
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Yihan Mei
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        descriptionText = new JTextPane();
        accountText = new JTextPane();
        accountEditText = new JTextField();
        passwordText = new JTextPane();
        textField2 = new JTextField();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();
        forgetButton = new JButton();

        //======== this ========
        setTitle("\u65e0\u7528\u7684\u804a\u5929\u8f6f\u4ef6");
        setResizable(false);
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
                    "23dlu, $lcgap, center:112dlu:grow",
                    "2*(default, $lgap), default"));

                //---- descriptionText ----
                descriptionText.setText("\u6b22\u8fce\u8bd5\u7528\u65e0\u7528\u7684\u804a\u5929\u8f6f\u4ef6");
                descriptionText.setEditable(false);
                descriptionText.setBackground(UIManager.getColor("window"));
                descriptionText.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
                contentPanel.add(descriptionText, CC.xywh(1, 1, 3, 1, CC.CENTER, CC.CENTER));

                //---- accountText ----
                accountText.setText("\u8d26\u53f7\uff1a");
                accountText.setEditable(false);
                accountText.setBackground(SystemColor.window);
                contentPanel.add(accountText, CC.xy(1, 3));

                //---- accountEditText ----
                accountEditText.setMinimumSize(new Dimension(100, 26));
                accountEditText.setPreferredSize(new Dimension(100, 26));
                contentPanel.add(accountEditText, CC.xy(3, 3, CC.FILL, CC.DEFAULT));

                //---- passwordText ----
                passwordText.setText("\u5bc6\u7801\uff1a");
                passwordText.setEditable(false);
                passwordText.setBackground(SystemColor.window);
                contentPanel.add(passwordText, CC.xy(1, 5));

                //---- textField2 ----
                textField2.setMinimumSize(new Dimension(12, 26));
                textField2.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                contentPanel.add(textField2, CC.xy(3, 5, CC.FILL, CC.DEFAULT));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(Borders.createEmptyBorder("4dlu, 0dlu, 0dlu, 0dlu"));
                buttonBar.setLayout(new FormLayout(
                    "$glue, [45dlu,pref], $rgap, 45dlu, $lcgap, [45dlu,pref], $lcgap, 0dlu",
                    "pref"));

                //---- okButton ----
                okButton.setText("\u767b\u9646");
                okButton.setMaximumSize(new Dimension(50, 27));
                okButton.setMinimumSize(new Dimension(50, 27));
                okButton.setPreferredSize(new Dimension(50, 27));
                buttonBar.add(okButton, CC.xy(2, 1, CC.FILL, CC.DEFAULT));

                //---- cancelButton ----
                cancelButton.setText("\u53d6\u6d88");
                buttonBar.add(cancelButton, CC.xy(4, 1));

                //---- forgetButton ----
                forgetButton.setText("\u5fd8\u8bb0\u5bc6\u7801");
                buttonBar.add(forgetButton, CC.xy(6, 1));
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
    private JTextPane descriptionText;
    private JTextPane accountText;
    private JTextField accountEditText;
    private JTextPane passwordText;
    private JTextField textField2;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    private JButton forgetButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
