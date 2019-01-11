package chatapp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JOptionPane;

/**
 *
 * @author Darag
 */
public class ClientController {
    
    private final ClientView theView;
    private final ClientModel theModel;
    private boolean buttonClicked = false; // a flag for enabling both clicking a button using a mouse and with enter key 

    public ClientController(ClientView theView, ClientModel theModel) {
        this.theView = theView;
        this.theModel = theModel;
        String clientName = theView.getClientName();
        theView.setFrameTitleToClientName(clientName);
        theModel.initSocketConnections(clientName);
        theView.showDialogBox(theModel.recive());
        theView.setVisible(true);
        theView.sendMessageButtonListener(new EventsHandler(), new EventsHandler());
    }
    
    public void reciveMessagesFromServer() {
        String line = "";
        while (true) {
            try {
                line = theModel.recive();
                if (line.contains(":")) {
                    theView.appendMessageToTextarea(line);
                } else if (line.contains("Arrived")) {
                    theView.showDialogBox(line);
                } else if (line.contains("Disconnected")) {
                    theView.showDialogBox(line);
                }
            } catch (Exception ex) {
                break;
            }
        }
    }
    
    private class EventsHandler implements ActionListener, KeyListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (buttonClicked == false) {
                try {
                    theModel.sendMessagesToServer(theView.getMessageText() + "\n");
                    theView.clearMessages();
                    
                } catch (Exception ex) {
                    System.out.println("error");
                }
            }
        }
        
        @Override
        public void keyTyped(KeyEvent e) {
        }
        
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                buttonClicked = true;
                theView.getButton().doClick();
                buttonClicked = false;
                try {
                    if (theView.getMessageText().equals("bye")) {
                        theView.dispose();
                        theModel.closeStreams();
                    }
                    
                    theModel.sendMessagesToServer(theView.getMessageText() + "\n");
                    theView.clearMessages();
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
