/*
 * Created by JFormDesigner on Mon Nov 06 22:59:24 SGT 2017
 */

package client.account.view;

import java.awt.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

/**
 * @author Yihan Mei
 */
public class MainDialog extends JFrame {
    public MainDialog() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Yihan Mei
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        currentUserDescription = new JTextPane();
        currentUserText = new JTextPane();
        scrollPane1 = new JScrollPane();
        usersList = new JList();
        endButton = new JButton();

        //======== this ========
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("\u6ca1\u7528\u7684\u804a\u5929\u8f6f\u4ef6");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(Borders.createEmptyBorder("9dlu, 9dlu, 9dlu, 9dlu"));
            dialogPane.setMinimumSize(new Dimension(400, 600));
            dialogPane.setPreferredSize(new Dimension(400, 600));

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
                    "34dlu, $lcgap, 88dlu, $lcgap, 0dlu:grow",
                    "default, $lgap, default:grow, $lgap, default"));

                //---- currentUserDescription ----
                currentUserDescription.setEditable(false);
                currentUserDescription.setBackground(SystemColor.window);
                currentUserDescription.setText("\u5f53\u524d\u7528\u6237\uff1a");
                contentPanel.add(currentUserDescription, CC.xy(1, 1));

                //---- currentUserText ----
                currentUserText.setEditable(false);
                currentUserText.setBackground(SystemColor.window);
                currentUserText.setText("sample");
                contentPanel.add(currentUserText, CC.xy(3, 1));

                //======== scrollPane1 ========
                {
                    scrollPane1.setPreferredSize(new Dimension(100, 240));
                    scrollPane1.setMinimumSize(new Dimension(100, 240));

                    //---- usersList ----
                    usersList.setMaximumSize(new Dimension(100, 240));
                    usersList.setMinimumSize(new Dimension(100, 240));
                    usersList.setPreferredSize(new Dimension(100, 240));
                    scrollPane1.setViewportView(usersList);
                }
                contentPanel.add(scrollPane1, CC.xywh(1, 3, 5, 1, CC.DEFAULT, CC.FILL));

                //---- endButton ----
                endButton.setText("\u767b\u51fa");
                contentPanel.add(endButton, CC.xy(5, 5));
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
    private JTextPane currentUserDescription;
    private JTextPane currentUserText;
    private JScrollPane scrollPane1;
    private JList usersList;
    private JButton endButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables


    public JPanel getDialogPane() {
        return dialogPane;
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    public JTextPane getCurrentUserDescription() {
        return currentUserDescription;
    }

    public JTextPane getCurrentUserText() {
        return currentUserText;
    }

    public JScrollPane getScrollPane1() {
        return scrollPane1;
    }

    public JList getUsersList() {
        return usersList;
    }

    public JButton getEndButton() {
        return endButton;
    }
}
