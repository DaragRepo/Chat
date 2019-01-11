package chatapp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author Darag
 */
public class ClientModel {

    private Socket connection;
    private static DataInputStream recieveMessage;
    private DataOutputStream sendMessage;

    public void initSocketConnections(String clientName) {
        try {
            connection = new Socket("localhost", 9090);
            recieveMessage = new DataInputStream(connection.getInputStream());
            sendMessage = new DataOutputStream(connection.getOutputStream());
            sendMessage.writeUTF(clientName);
        } catch (Exception ex) {
            System.out.println("error");
        }

    }

    public void sendMessagesToServer(String message) {
        try {
            sendMessage.writeUTF(message);
        } catch (Exception ex) {
            System.out.println("error5");
        }
    }

    public String recive() {
        try {
            return recieveMessage.readUTF();
        } catch (Exception ex) {
            System.out.println("error");
        }
        return null;
    }

    public void closeStreams() {
        try {
            recieveMessage.close();
            sendMessage.close();
            connection.close();
        } catch (Exception ex) {
            System.out.println("error");
        }
    }

}
