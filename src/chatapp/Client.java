package chatapp;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author Darag
 */
public class Client extends JFrame {

    private Socket connection;
    private String messages;
    private static DataInputStream recieveMessage;
    private DataOutputStream sendMessage;
    private static JTextArea displayMessages = new JTextArea(17, 50);
    private GUIPanel panel = new GUIPanel();
    private String name;
    private boolean clicked = false;

    public Client() {
         setFrameSettings();
    }

    private void initSocketConnections() {
        try {
            connection = new Socket("localhost", 9090);
            recieveMessage = new DataInputStream(connection.getInputStream());
            sendMessage = new DataOutputStream(connection.getOutputStream());
            sendMessage.writeUTF(name);
            JOptionPane.showMessageDialog(null, recieveMessage.readUTF());
        } catch (Exception ex) {
            System.out.println("error");
        }

    }

    private void getClientName() {
        name = JOptionPane.showInputDialog(
                this,
                "Please Enter Your Name to continue:");
    }

    private void setFrameSettings() {
        setTitle("GUI");
        JScrollPane scroll = new JScrollPane(displayMessages);
        displayMessages.setEditable(false);
        //    this.setEnabled(false);
        add(scroll, BorderLayout.NORTH);
        scroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                e.getAdjustable().setValue(e.getAdjustable().getMaximum());
            }
        });
        add(panel, BorderLayout.SOUTH);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        //    setVisible(true);
    }

    private final class GUIPanel extends JPanel {

        private JTextField writeMessages = new JTextField(30);
        private JButton sendMessages = new JButton("Send");

        public GUIPanel() {

            writeMessages.setFocusable(true);
            add(writeMessages);
            //sendMessages.setFocusable(false);
            sendMessages.addActionListener(new EventsHandler());
            writeMessages.addKeyListener(new EventsHandler());

            add(sendMessages);

        }

        private class EventsHandler implements ActionListener, KeyListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource().equals(sendMessages)) {
                    if (clicked == false) {
                        try {
                            sendMessage.writeUTF(writeMessages.getText() + "\n");
                            writeMessages.setText("");

                        } catch (Exception ex) {
                            System.out.println("error");
                        }
                    }
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    clicked = true;
                    sendMessages.doClick();
                    clicked = false;
                    try {
                        if (writeMessages.getText().equals("bye")) {
                           dispose();
                           recieveMessage.close();
                           sendMessage.close();
                           try {
                               connection.close();
                           } catch (Exception w) {System.out.println("error");}
                        }

                        sendMessage.writeUTF(writeMessages.getText() + "\n");
                        writeMessages.setText("");
                    } catch (Exception ex) {
                        System.out.println("error");
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

        }

    }

    public static void main(String[] args) {
        Client client = new Client();
        client.getClientName();
        client.setVisible(true);
        client.initSocketConnections();
        String line = "";
        while (true) {
            try {
                line = recieveMessage.readUTF();
                if (line.contains(":")) {
                    displayMessages.append(line + " \n");
                } else if (line.contains("Arrived")) {
                    JOptionPane.showMessageDialog(null, line);
                } else if (line.contains("Disconnected")) {
                    JOptionPane.showMessageDialog(null, line);
                }
            } catch (Exception ex) {
               break;
            }
        }

    }

}
