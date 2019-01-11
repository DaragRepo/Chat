package chatapp;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Darag
 */
public class ClientView extends JFrame {

    private static final JTextArea displayMessages = new JTextArea(17, 50);

    private final JTextField writeMessages = new JTextField(30);
    private final JButton sendMessages = new JButton("Send");

    public ClientView() {
        setFrameSettings();
        writeMessages.setFocusable(true);
        JPanel clientPanel = new JPanel();
        clientPanel.add(writeMessages);
        clientPanel.add(sendMessages);
        add(clientPanel, BorderLayout.SOUTH);
    }

    public String getClientName() {
        return JOptionPane.showInputDialog(
                this,
                "Please Enter Your Name to continue:");
    }

    public JButton getButton() {
        return sendMessages;
    }

    private void setFrameSettings() {
        displayMessages.setEditable(false);
        JScrollPane scroll = new JScrollPane(displayMessages);
        add(scroll, BorderLayout.NORTH);
        scroll.getVerticalScrollBar().addAdjustmentListener((AdjustmentEvent e) -> {
            e.getAdjustable().setValue(e.getAdjustable().getMaximum());
        });
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        //    setVisible(true);
    }

    public void sendMessageButtonListener(ActionListener buttonClicked, KeyListener keyPressed) {
        sendMessages.addActionListener(buttonClicked);
        writeMessages.addKeyListener(keyPressed);
    }

    public void clearMessages() {
        writeMessages.setText("");
    }

    public String getMessageText() {
        return writeMessages.getText();
    }

    public void disposeOfFrame() {
        dispose();
    }

    public void appendMessageToTextarea(String message) {
        displayMessages.append(message + " \n");
    }

    public void showDialogBox(String text) {
        JOptionPane.showMessageDialog(null, text);
    }

    public void setFrameTitleToClientName(String clientName) {
        this.setTitle(clientName);
    }
}
