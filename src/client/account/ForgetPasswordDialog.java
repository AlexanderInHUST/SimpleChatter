/*
 * Created by JFormDesigner on Mon Nov 06 12:13:19 SGT 2017
 */

package client.account;

import java.awt.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

/**
 * @author Yihan Mei
 */
public class ForgetPasswordDialog extends JDialog {
    public ForgetPasswordDialog(Frame owner) {
        super(owner);
        initComponents();
    }

    public ForgetPasswordDialog(Dialog owner) {
        super(owner);
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Yihan Mei
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        textPane1 = new JTextPane();
        textPane2 = new JTextPane();
        textField1 = new JTextField();
        textPane3 = new JTextPane();
        textField2 = new JTextField();
        button1 = new JButton();
        textPane4 = new JTextPane();
        textField4 = new JTextField();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
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
                    "36dlu, $lcgap, default:grow",
                    "4*(default, $lgap), default"));
                contentPanel.add(textPane1, CC.xywh(1, 1, 3, 1));
                contentPanel.add(textPane2, CC.xy(1, 3));
                contentPanel.add(textField1, CC.xy(3, 3));
                contentPanel.add(textPane3, CC.xy(1, 5));
                contentPanel.add(textField2, CC.xy(3, 5));

                //---- button1 ----
                button1.setText("text");
                contentPanel.add(button1, CC.xy(3, 7, CC.RIGHT, CC.DEFAULT));
                contentPanel.add(textPane4, CC.xy(1, 9));
                contentPanel.add(textField4, CC.xy(3, 9));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(Borders.createEmptyBorder("4dlu, 0dlu, 0dlu, 0dlu"));
                buttonBar.setLayout(new FormLayout(
                    "$glue, $button, $rgap, $button",
                    "pref"));

                //---- okButton ----
                okButton.setText("OK");
                buttonBar.add(okButton, CC.xy(2, 1));

                //---- cancelButton ----
                cancelButton.setText("Cancel");
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
    private JTextPane textPane1;
    private JTextPane textPane2;
    private JTextField textField1;
    private JTextPane textPane3;
    private JTextField textField2;
    private JButton button1;
    private JTextPane textPane4;
    private JTextField textField4;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
